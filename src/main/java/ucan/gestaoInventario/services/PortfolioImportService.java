package ucan.gestaoInventario.services;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ucan.gestaoInventario.dto.rto.relatorio.MensagemTipo;
import ucan.gestaoInventario.dto.rto.relatorio.Relatorio;
import ucan.gestaoInventario.dto.rto.relatorio.RepositorioItem;
import ucan.gestaoInventario.entities.Portfolio;
import ucan.gestaoInventario.entities.Produto;
import ucan.gestaoInventario.entities.Versao;
import ucan.gestaoInventario.repositories.PortfolioRepository;
import ucan.gestaoInventario.repositories.ProdutoRepository;
import ucan.gestaoInventario.repositories.VersaoRepository;

import java.io.InputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PortfolioImportService
{

    private static final String PK_VERSAO_PORTFOLIO = "PORTFOLIO";
    private static final ZoneId ZONE = ZoneId.of("Africa/Luanda");

    private final PortfolioRepository portfolioRepository;
    private final ProdutoRepository produtoRepository;
    private final VersaoRepository versaoRepository;

    public PortfolioImportService(
        PortfolioRepository portfolioRepository,
        ProdutoRepository produtoRepository,
        VersaoRepository versaoRepository
    )
    {
        this.portfolioRepository = portfolioRepository;
        this.produtoRepository = produtoRepository;
        this.versaoRepository = versaoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Relatorio importar(MultipartFile file) throws Exception
    {
        validarFicheiro(file);

        Relatorio rel = new Relatorio();
        DataFormatter fmt = new DataFormatter();

        try (InputStream is = file.getInputStream(); Workbook wb = WorkbookFactory.create(is))
        {
            Sheet sheet = wb.getSheetAt(0);

            // 1) LER DATA
            LocalDateTime dataExcel = lerData(sheet, fmt);
            rel.setDataExcel(dataExcel);

            LocalDateTime dataBD = obterDataBD();
            rel.setDataBD(dataBD);

            // 1.1) VALIDAR DATA SEM EXCEPTION (para não dar 500)
            if (!validarDataOuInterromper(dataExcel, dataBD, rel))
            {
                rel.getMensagemLista().sort();
                rel.setPersistido(false);
                return rel;
            }

            // 2) ler tabela (gera itens + mensagens)
            int headerRow = procurarCabecalho(sheet, fmt);
            List<RepositorioItem> itens = lerItens(sheet, fmt, headerRow, rel);

            // 3) validações (não interrompem)
            validarDuplicadosEOrdenacao(itens, rel);
            validarPaisExistentes(itens, rel);
            validarFolhasProdutos(itens, rel);

            // (opcional) devolver os itens mesmo com erro
            rel.setRepositorioItemLista(itens);

            // 4) se há erros -> retorna sem persistir
            if (rel.getMensagemLista().temErros())
            {
                rel.getMensagemLista().sort();
                rel.setPersistido(false);
                return rel;
            }

            // 5) persistir (somente se não há erros)
            persistirPortfolio(itens);
            persistirProdutos(itens);
            upsertVersao(dataExcel);

            rel.setPersistido(true);
            return rel;
        }
    }

    // =========================================================
    // VALIDAR ficheiro
    // =========================================================
    private void validarFicheiro(MultipartFile file)
    {
        if (file == null || file.isEmpty())
        {
            throw new IllegalArgumentException("Ficheiro Excel não foi enviado (vazio).");
        }
    }

    private LocalDateTime obterDataBD()
    {
        return versaoRepository.findById(PK_VERSAO_PORTFOLIO)
            .map(Versao::getData)
            .orElse(null);
    }

    // SEM EXCEPTION: grava no Relatorio e retorna false
    private boolean validarDataOuInterromper(LocalDateTime excel, LocalDateTime bd, Relatorio rel)
    {
        if (bd == null)
        {
            return true; // primeira importação
        }

        if (!excel.isAfter(bd))
        {
            rel.getMensagemLista().add(
                0,
                MensagemTipo.ERRO,
                "Importação bloqueada: a data do Excel (" + excel + ") é menor/igual à data da BD (" + bd + ")."
            );
            return false;
        }

        return true;
    }

    // =========================================================
    // LER DATA
    // =========================================================
    private LocalDateTime lerData(Sheet sheet, DataFormatter fmt)
    {
        for (Row row : sheet)
        {
            if (row == null)
            {
                continue;
            }

            String a = str(fmt.formatCellValue(row.getCell(0)));
            if (a == null)
            {
                continue;
            }

            if (!"DATA".equalsIgnoreCase(a))
            {
                continue;
            }

            Cell c1 = row.getCell(1);
            if (c1 == null)
            {
                throw new IllegalStateException("Existe 'DATA' no Excel, mas a célula da data (coluna B) está vazia.");
            }

            if (c1.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c1))
            {
                Date d = c1.getDateCellValue();
                return LocalDateTime.ofInstant(d.toInstant(), ZONE);
            }

            String texto = str(fmt.formatCellValue(c1));
            if (texto == null)
            {
                throw new IllegalStateException("A data do Excel (linha DATA) está vazia.");
            }

            return parseData(texto);
        }

        throw new IllegalStateException("Não encontrei a linha/célula 'DATA' no Excel.");
    }

    private LocalDateTime parseData(String s)
    {
        String normal = s.trim();

        // tenta ISO_LOCAL_DATE_TIME ("2026-01-11T18:00:00")
        try
        {
            return LocalDateTime.parse(normal.replace(" ", "T"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        catch (Exception ignored)
        {
        }

        List<DateTimeFormatter> formatos = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ISO_LOCAL_DATE
        );

        for (DateTimeFormatter f : formatos)
        {
            try
            {
                if (f == DateTimeFormatter.ISO_LOCAL_DATE)
                {
                    LocalDate ld = LocalDate.parse(normal, f);
                    return ld.atStartOfDay();
                }
                return LocalDateTime.parse(normal, f);
            }
            catch (Exception ignored)
            {
            }
        }

        throw new IllegalStateException("Não foi possível interpretar a data do Excel: '" + s + "'.");
    }

    // =========================================================
    // CABEÇALHO
    // =========================================================
    private int procurarCabecalho(Sheet sheet, DataFormatter fmt)
    {
        for (Row row : sheet)
        {
            if (row == null)
            {
                continue;
            }

            String c0 = nz(fmt.formatCellValue(row.getCell(0)));
            String c1 = nz(fmt.formatCellValue(row.getCell(1)));
            String c2 = nz(fmt.formatCellValue(row.getCell(2)));
            String c3 = nz(fmt.formatCellValue(row.getCell(3)));

            boolean ok12 = "codigo".equalsIgnoreCase(c0) && "descricao".equalsIgnoreCase(c1);
            boolean ok34 = "quantidade_critica".equalsIgnoreCase(c2) && "quantidade_maxima".equalsIgnoreCase(c3);

            if (ok12 && ok34)
            {
                return row.getRowNum();
            }
        }

        throw new IllegalStateException("Cabeçalho não encontrado. Esperado: codigo | descricao | quantidade_critica | quantidade_maxima");
    }

    // =========================================================
    // LER ITENS
    // =========================================================
    private List<RepositorioItem> lerItens(Sheet sheet, DataFormatter fmt, int headerRow, Relatorio rel)
    {
        List<RepositorioItem> itens = new ArrayList<>();
        int last = sheet.getLastRowNum();

        for (int r = headerRow + 1; r <= last; r++)
        {
            Row row = sheet.getRow(r);
            if (row == null)
            {
                continue;
            }

            int linhaExcel = r + 1;

            String codigo = str(fmt.formatCellValue(row.getCell(0)));
            String descricao = str(fmt.formatCellValue(row.getCell(1)));

            Integer qtdCrit = parseIntComMensagem(row.getCell(2), fmt, rel, linhaExcel, "quantidade_critica");
            Integer qtdMax = parseIntComMensagem(row.getCell(3), fmt, rel, linhaExcel, "quantidade_maxima");

            // linha toda vazia => descarta
            if (codigo == null && descricao == null && qtdCrit == null && qtdMax == null)
            {
                continue;
            }

            // obrigatório faltando mas há algo => ERRO (não descarta)
            if (codigo == null || descricao == null)
            {
                rel.getMensagemLista().add(linhaExcel, MensagemTipo.ERRO,
                    "Linha inválida: 'codigo' e 'descricao' são obrigatórios (linha não pode ser descartada).");
                continue;
            }

            RepositorioItem it = new RepositorioItem();
            it.setLinha(linhaExcel);
            it.setCodigo(codigo);
            it.setDesignacao(descricao);
            it.setQuantidadeCritica(qtdCrit);
            it.setQuantidadeMaxima(qtdMax);

            itens.add(it);
        }

        return itens;
    }

    private Integer parseIntComMensagem(Cell cell, DataFormatter fmt, Relatorio rel, int linha, String campo)
    {
        if (cell == null)
        {
            return null;
        }

        try
        {
            if (cell.getCellType() == CellType.NUMERIC)
            {
                double v = cell.getNumericCellValue();
                if (v % 1 != 0)
                {
                    rel.getMensagemLista().add(linha, MensagemTipo.ERRO,
                        "Campo '" + campo + "' tem casas decimais: " + v);
                    return null;
                }
                return (int) v;
            }

            String s = str(fmt.formatCellValue(cell));
            if (s == null)
            {
                return null;
            }

            return Integer.parseInt(s);
        }
        catch (Exception e)
        {
            rel.getMensagemLista().add(linha, MensagemTipo.ERRO,
                "Campo '" + campo + "' inválido.");
            return null;
        }
    }

    // =========================================================
    // VALIDAÇÕES (NÃO INTERROMPEM)
    // =========================================================
    private void validarDuplicadosEOrdenacao(List<RepositorioItem> itens, Relatorio rel)
    {
        Set<String> seen = new HashSet<>();
        RepositorioItem prev = null;

        for (RepositorioItem it : itens)
        {
            if (!seen.add(it.getCodigo()))
            {
                rel.getMensagemLista().add(it.getLinha(), MensagemTipo.ERRO,
                    "Código duplicado no Excel: " + it.getCodigo());
            }

            if (prev != null)
            {
                int cmp = compareCodigo(prev.getCodigo(), it.getCodigo());
                if (cmp > 0)
                {
                    rel.getMensagemLista().add(it.getLinha(), MensagemTipo.ERRO,
                        "Código fora de ordem crescente. Anterior='" + prev.getCodigo() + "', Atual='" + it.getCodigo() + "'");
                }
            }

            prev = it;
        }
    }

    private void validarPaisExistentes(List<RepositorioItem> itens, Relatorio rel)
    {
        Set<String> codigos = new HashSet<>();
        for (RepositorioItem it : itens)
        {
            codigos.add(it.getCodigo());
        }

        for (RepositorioItem it : itens)
        {
            String pai = codigoPai(it.getCodigo());
            if (pai == null)
            {
                continue;
            }

            if (!codigos.contains(pai))
            {
                rel.getMensagemLista().add(it.getLinha(), MensagemTipo.ERRO,
                    "Pai inexistente para o código '" + it.getCodigo() + "'. Pai esperado: '" + pai + "'");
            }
        }
    }

    private void validarFolhasProdutos(List<RepositorioItem> itens, Relatorio rel)
    {
        Set<String> codigos = new HashSet<>();
        for (RepositorioItem it : itens)
        {
            codigos.add(it.getCodigo());
        }

        for (RepositorioItem it : itens)
        {
            boolean folha = !temFilho(codigos, it.getCodigo());
            if (!folha)
            {
                continue;
            }

            if (it.getQuantidadeCritica() == null && it.getQuantidadeMaxima() == null)
            {
                continue;
            }
            if (it.getQuantidadeCritica() == null)
            {
                rel.getMensagemLista().add(it.getLinha(), MensagemTipo.ERRO,
                    "Falta a quantidade_critica no produto. Código: " + it.getCodigo());
                continue;
            }
            if (it.getQuantidadeMaxima() == null)
            {
                rel.getMensagemLista().add(it.getLinha(), MensagemTipo.ERRO,
                    "Falta a quantidade_maxima no produto. Código: " + it.getCodigo());
                continue;
            }
            if (it.getQuantidadeMaxima() < it.getQuantidadeCritica())
            {
                rel.getMensagemLista().add(it.getLinha(), MensagemTipo.ERRO,
                    "quantidade_maxima < quantidade_critica no produto. Código: " + it.getCodigo());
            }
        }
    }

    // =========================================================
    // PERSISTÊNCIA
    // =========================================================
    private void persistirPortfolio(List<RepositorioItem> itens)
    {
        Set<String> codigos = new HashSet<>();
        for (RepositorioItem it : itens)
        {
            codigos.add(it.getCodigo());
        }

        List<Portfolio> existentes = portfolioRepository.findAllById(codigos);
        Map<String, Portfolio> map = new HashMap<>();
        for (Portfolio p : existentes)
        {
            map.put(p.getPkPortfolio(), p);
        }

        List<Portfolio> salvar = new ArrayList<>();

        for (RepositorioItem it : itens)
        {
            Portfolio p = map.get(it.getCodigo());
            if (p == null)
            {
                p = new Portfolio();
                p.setPkPortfolio(it.getCodigo());
            }

            p.setDescricao(it.getDesignacao());

            String paiCodigo = codigoPai(it.getCodigo());
            if (paiCodigo == null)
            {
                p.setFkPortfolioPai(null);
            }
            else
            {
                p.setFkPortfolioPai(portfolioRepository.getReferenceById(paiCodigo));
            }

            salvar.add(p);
        }

        portfolioRepository.saveAll(salvar);
    }

    private void persistirProdutos(List<RepositorioItem> itens)
    {
        // descobrir folhas
        Set<String> codigos = new HashSet<>();
        for (RepositorioItem it : itens)
        {
            if (it.getCodigo() != null)
            {
                codigos.add(it.getCodigo().trim());
            }
        }

        Set<String> folhas = new HashSet<>();
        for (String c : codigos)
        {
            if (!temFilho(codigos, c))
            {
                folhas.add(c);
            }
        }

        // buscar existentes só das folhas
        List<Produto> existentes = produtoRepository.findAllById(folhas);
        Map<String, Produto> map = new HashMap<>();
        for (Produto pr : existentes)
        {
            map.put(pr.getFkPortfolio(), pr);
        }

        List<Produto> salvar = new ArrayList<>();

        for (RepositorioItem it : itens)
        {
            String codigo = it.getCodigo();
            if (codigo == null)
            {
                continue;
            }

            codigo = codigo.trim();
            if (codigo.isEmpty())
            {
                continue;
            }

            if (!folhas.contains(codigo))
            {
                continue;
            }

            if (it.getQuantidadeCritica() == null || it.getQuantidadeMaxima() == null)
            {
                continue;
            }

            Produto pr = map.get(codigo);
            if (pr == null)
            {
                pr = new Produto();
            }

            pr.setFkPortfolio(codigo);
            pr.setQuantidadeCritica(it.getQuantidadeCritica());
            pr.setQuantidadeMaxima(it.getQuantidadeMaxima());

            salvar.add(pr);
        }

        produtoRepository.saveAll(salvar);
    }

    private void upsertVersao(LocalDateTime dataExcel)
    {
        Versao v = versaoRepository.findById(PK_VERSAO_PORTFOLIO)
            .orElseGet(() ->
            {
                Versao nova = new Versao();
                nova.setPkVersao(PK_VERSAO_PORTFOLIO);
                return nova;
            });

        v.setData(dataExcel);
        versaoRepository.save(v);
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private String str(String s)
    {
        if (s == null)
        {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private String nz(String s)
    {
        return s == null ? "" : s.trim();
    }

    private boolean temFilho(Set<String> codigos, String codigo)
    {
        String pref = codigo + ".";
        for (String c : codigos)
        {
            if (c.startsWith(pref))
            {
                return true;
            }
        }
        return false;
    }

    private String codigoPai(String codigo)
    {
        int idx = codigo.lastIndexOf('.');
        if (idx < 0)
        {
            return null;
        }
        return codigo.substring(0, idx);
    }

    private int compareCodigo(String a, String b)
    {
        String[] p1 = a.split("\\.");
        String[] p2 = b.split("\\.");

        int len = Math.max(p1.length, p2.length);

        for (int i = 0; i < len; i++)
        {
            if (i >= p1.length)
            {
                return -1;
            }
            if (i >= p2.length)
            {
                return 1;
            }

            int n1 = parseIntSafe(p1[i]);
            int n2 = parseIntSafe(p2[i]);

            int diff = Integer.compare(n1, n2);
            if (diff != 0)
            {
                return diff;
            }
        }

        return 0;
    }

    private int parseIntSafe(String s)
    {
        try
        {
            return Integer.parseInt(s.trim());
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}

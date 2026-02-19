package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ucan.gestaoInventario.dto.rto.ProdutoPrecoRequestDTO;
import ucan.gestaoInventario.dto.rto.ProdutoPrecoResponseRTO;
import ucan.gestaoInventario.dto.rto.ProdutoResponseRTO;
import ucan.gestaoInventario.entities.Produto;
import ucan.gestaoInventario.entities.ProdutoPreco;
import ucan.gestaoInventario.repositories.MovimentacaoInventarioRepository;
import ucan.gestaoInventario.repositories.ProdutoPrecoRepository;
import ucan.gestaoInventario.repositories.ProdutoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProdutoService
{

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoInventarioRepository movRepository;
    private final ProdutoPrecoRepository precoRepository;

    public ProdutoService(
        ProdutoRepository produtoRepository,
        MovimentacaoInventarioRepository movRepository,
        ProdutoPrecoRepository precoRepository
    )
    {
        this.produtoRepository = produtoRepository;
        this.movRepository = movRepository;
        this.precoRepository = precoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseRTO> listar(String nome, String prefixo, String estado)
    {
        nome = trimOrNull(nome);
        prefixo = trimOrNull(prefixo);
        estado = trimOrNull(estado);

        List<Produto> lista;

        if (estado != null)
        {
            String e = estado.toUpperCase(Locale.ROOT);

            if ("ABAIXO_CRITICO".equals(e))
            {
                lista = produtoRepository.listarAbaixoDoCritico();
            }
            else if ("ACIMA_MAXIMO".equals(e))
            {
                lista = produtoRepository.listarAcimaDoMaximo();
            }
            else
            {
                throw new IllegalArgumentException("estado inválido. Use ABAIXO_CRITICO ou ACIMA_MAXIMO.");
            }
        }
        else if (nome != null)
        {
            lista = produtoRepository.pesquisarPorNome(nome);
        }
        else if (prefixo != null)
        {
            lista = produtoRepository.listarPorPrefixo(prefixo);
        }
        else
        {
            lista = produtoRepository.findAll();
        }

        return lista.stream().map(this::toRTO).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseRTO detalhar(String codigo)
    {
        codigo = trimOrNull(codigo);
        if (codigo == null)
        {
            throw new IllegalArgumentException("codigo é obrigatório");
        }

        Produto pr = produtoRepository.buscarComPortfolio(codigo);
        if (pr == null)
        {
            throw new IllegalStateException("Produto não encontrado: " + codigo);
        }

        return toRTO(pr);
    }

    // altera preço e mantém histórico
    @Transactional
    public ProdutoResponseRTO alterarPreco(String codigo, ProdutoPrecoRequestDTO dto)
    {
        codigo = trimOrNull(codigo);
        if (codigo == null)
        {
            throw new IllegalArgumentException("codigo é obrigatório");
        }

        Double precoNovo = (dto == null) ? null : dto.getPreco();
        if (precoNovo == null || precoNovo <= 0)
        {
            throw new IllegalArgumentException("preco deve ser > 0");
        }

        precoNovo = normalizar2Casas(precoNovo);

        // NÃO usar lambda aqui (evita "effectively final")
        Produto pr = produtoRepository.buscarComPortfolio(codigo);
        if (pr == null)
        {
            throw new IllegalStateException("Produto não encontrado: " + codigo);
        }

        LocalDateTime agora = LocalDateTime.now();

        // fecha o preço atual primeiro (UPDATE direto)
        precoRepository.fecharPrecoAtual(codigo, agora);

        // cria novo preço atual
        ProdutoPreco novo = new ProdutoPreco();
        novo.setProduto(pr);
        novo.setPreco(precoNovo);
        novo.setDataInicio(agora);
        novo.setDataFim(null);

        precoRepository.save(novo);

        return toRTO(pr);
    }

    // histórico
    @Transactional(readOnly = true)
    public List<ProdutoPrecoResponseRTO> listarHistoricoPrecos(String codigo)
    {
        codigo = trimOrNull(codigo);
        if (codigo == null)
        {
            throw new IllegalArgumentException("codigo é obrigatório");
        }

        // valida se produto existe
        Produto pr = produtoRepository.buscarComPortfolio(codigo);
        if (pr == null)
        {
            throw new IllegalStateException("Produto não encontrado: " + codigo);
        }

        return precoRepository.findByProduto_FkPortfolioOrderByDataInicioDesc(codigo)
            .stream()
            .map(pp -> new ProdutoPrecoResponseRTO(
            pp.getPkPreco(),
            pp.getProduto().getFkPortfolio(), // assim não depende do "codigo" de fora
            pp.getPreco(),
            pp.getDataInicio(),
            pp.getDataFim()
        ))
            .toList();
    }

    private ProdutoResponseRTO toRTO(Produto pr)
    {
        String codigo = pr.getFkPortfolio();
        String descricao = pr.getPortfolio().getDescricao();
        Integer stock = movRepository.calcularStock(codigo);

        Double preco = precoRepository
            .findFirstByProduto_FkPortfolioAndDataFimIsNullOrderByDataInicioDesc(codigo)
            .map(ProdutoPreco::getPreco)
            .orElse(null);

        return new ProdutoResponseRTO(
            codigo,
            descricao,
            pr.getQuantidadeCritica(),
            pr.getQuantidadeMaxima(),
            stock,
            preco
        );
    }

    private String trimOrNull(String s)
    {
        if (s == null)
        {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private Double normalizar2Casas(Double v)
    {
        return Math.round(v * 100.0) / 100.0;
    }
}

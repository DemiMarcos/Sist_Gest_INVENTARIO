package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ucan.gestaoInventario.dto.rto.*;
import ucan.gestaoInventario.entities.*;
import ucan.gestaoInventario.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService
{

    private final VendaRepository vendaRepository;
    private final VendaItemRepository vendaItemRepository;
    private final PessoaRepository pessoaRepository;
    private final PessoaTipoMapRepository pessoaTipoMapRepository;

    private final PortfolioRepository portfolioRepository;
    private final MovimentacaoInventarioRepository movRepository;

    private final ArmazemRepository armazemRepo;
    private final LocalizacaoRepository localizacaoRepo;
    private final StockService stockService;

    public VendaService(
        VendaRepository vendaRepository,
        VendaItemRepository vendaItemRepository,
        PessoaRepository pessoaRepository,
        PessoaTipoMapRepository pessoaTipoMapRepository,
        PortfolioRepository portfolioRepository,
        MovimentacaoInventarioRepository movRepository,
        ArmazemRepository armazemRepo,
        LocalizacaoRepository localizacaoRepo,
        StockService stockService
    )
    {
        this.vendaRepository = vendaRepository;
        this.vendaItemRepository = vendaItemRepository;
        this.pessoaRepository = pessoaRepository;
        this.pessoaTipoMapRepository = pessoaTipoMapRepository;
        this.portfolioRepository = portfolioRepository;
        this.movRepository = movRepository;
        this.armazemRepo = armazemRepo;
        this.localizacaoRepo = localizacaoRepo;
        this.stockService = stockService;
    }

    // =========================
    // CRIAR VENDA (ABERTA)
    // =========================
    @Transactional
    public VendaResponseRTO criar(VendaCreateRequestDTO dto)
    {
        if (dto == null || dto.getFkCliente() == null)
        {
            throw new IllegalArgumentException("fkCliente é obrigatório");
        }

        Pessoa cliente = pessoaRepository.findById(dto.getFkCliente())
            .orElseThrow(() -> new IllegalStateException("Cliente não encontrado: " + dto.getFkCliente()));

        if (Boolean.FALSE.equals(cliente.getActivo()))
        {
            throw new IllegalStateException("Cliente está inactivo: " + cliente.getPkPessoa());
        }

        boolean isCliente = pessoaTipoMapRepository.existeTipo(cliente.getPkPessoa(), PessoaBaseService.TIPO_CLIENTE);
        if (!isCliente)
        {
            throw new IllegalStateException("Pessoa " + cliente.getPkPessoa() + " não é do tipo CLIENTE");
        }

        Venda v = new Venda();
        v.setDataVenda(LocalDateTime.now());
        v.setEstado("ABERTA");
        v.setCliente(cliente);

        v = vendaRepository.save(v);
        return toRTO(v);
    }

    @Transactional(readOnly = true)
    public List<VendaResponseRTO> listar()
    {
        return vendaRepository.listarComCliente()
            .stream()
            .map(this::toRTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public VendaResponseRTO detalhar(Integer id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id é obrigatório");
        }

        Venda v = vendaRepository.buscarComCliente(id);
        if (v == null)
        {
            throw new IllegalStateException("Venda não encontrada: " + id);
        }

        return toRTO(v);
    }

    // =========================
    // ITENS - ADICIONAR
    // =========================
    @Transactional
    public VendaItemResponseRTO adicionarItem(Integer idVenda, VendaItemCreateDTO dto)
    {
        if (idVenda == null)
        {
            throw new IllegalArgumentException("idVenda é obrigatório");
        }
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }

        Venda v = vendaRepository.findById(idVenda)
            .orElseThrow(() -> new IllegalStateException("Venda não encontrada: " + idVenda));

        if (!"ABERTA".equalsIgnoreCase(v.getEstado()))
        {
            throw new IllegalStateException("Só é possível adicionar itens quando a venda está ABERTA");
        }

        String fk = trim(dto.getFkPortfolio());
        if (fk == null)
        {
            throw new IllegalArgumentException("fkPortfolio é obrigatório");
        }

        Portfolio p = portfolioRepository.findById(fk)
            .orElseThrow(() -> new IllegalStateException("Portfólio não encontrado: " + fk));

        if (dto.getQuantidade() == null || dto.getQuantidade() < 1)
        {
            throw new IllegalArgumentException("quantidade deve ser >= 1");
        }

        if (dto.getPrecoUnitario() == null || dto.getPrecoUnitario() <= 0)
        {
            throw new IllegalArgumentException("precoUnitario deve ser > 0");
        }

        VendaItem vi = new VendaItem();
        vi.setVenda(v);
        vi.setPortfolio(p);
        vi.setQuantidade(dto.getQuantidade());
        vi.setPrecoUnitario(normalizar2Casas(dto.getPrecoUnitario()));

        vi = vendaItemRepository.save(vi);

        return new VendaItemResponseRTO(
            vi.getPkItem(),
            p.getPkPortfolio(),
            p.getDescricao(),
            vi.getQuantidade(),
            vi.getPrecoUnitario()
        );
    }

    @Transactional(readOnly = true)
    public List<VendaItemResponseRTO> listarItens(Integer idVenda)
    {
        if (idVenda == null)
        {
            throw new IllegalArgumentException("idVenda é obrigatório");
        }

        if (vendaRepository.findById(idVenda).isEmpty())
        {
            throw new IllegalStateException("Venda não encontrada: " + idVenda);
        }

        return vendaItemRepository.listarItensDaVenda(idVenda)
            .stream()
            .map(it -> new VendaItemResponseRTO(
            it.getPkItem(),
            it.getPortfolio().getPkPortfolio(),
            it.getPortfolio().getDescricao(),
            it.getQuantidade(),
            it.getPrecoUnitario()
        ))
            .toList();
    }

    @Transactional
    public void removerItem(Integer idVenda, Integer pkItem)
    {
        if (idVenda == null || pkItem == null)
        {
            throw new IllegalArgumentException("idVenda e pkItem são obrigatórios");
        }

        Venda v = vendaRepository.findById(idVenda)
            .orElseThrow(() -> new IllegalStateException("Venda não encontrada: " + idVenda));

        if (!"ABERTA".equalsIgnoreCase(v.getEstado()))
        {
            throw new IllegalStateException("Só é possível remover itens quando a venda está ABERTA");
        }

        VendaItem it = vendaItemRepository.findById(pkItem)
            .orElseThrow(() -> new IllegalStateException("Item não encontrado: " + pkItem));

        if (!it.getVenda().getPkVenda().equals(idVenda))
        {
            throw new IllegalStateException("Este item não pertence à venda " + idVenda);
        }

        vendaItemRepository.delete(it);
    }

    // =========================
    // CONFIRMAR VENDA (gera S + atualiza snapshot)
    // Params: fkArmazem (obrigatório), fkLocalizacao (opcional)
    // =========================
    @Transactional
    public VendaResponseRTO confirmar(Integer idVenda, Integer fkArmazem, Integer fkLocalizacao)
    {
        if (idVenda == null)
        {
            throw new IllegalArgumentException("idVenda é obrigatório");
        }
        if (fkArmazem == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório");
        }

        Venda v = vendaRepository.findById(idVenda)
            .orElseThrow(() -> new IllegalStateException("Venda não encontrada: " + idVenda));

        if (!"ABERTA".equalsIgnoreCase(v.getEstado()))
        {
            throw new IllegalStateException("Só é possível confirmar venda com estado ABERTA");
        }

        List<VendaItem> itens = vendaItemRepository.listarItensDaVenda(idVenda);
        if (itens == null || itens.isEmpty())
        {
            throw new IllegalStateException("Não é possível confirmar venda sem itens");
        }

        Armazem armazem = armazemRepo.findById(fkArmazem)
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + fkArmazem));

        if (Boolean.FALSE.equals(armazem.getActivo()))
        {
            throw new IllegalStateException("Armazém está inactivo: " + armazem.getPkArmazem());
        }

        Localizacao loc = null;
        if (fkLocalizacao != null)
        {
            loc = localizacaoRepo
                .findByArmazem_PkArmazemAndPkLocalizacao(fkArmazem, fkLocalizacao)
                .orElseThrow(() -> new IllegalStateException(
                "Localização não encontrada ou não pertence ao armazém. fkArmazem="
                + fkArmazem + " fkLocalizacao=" + fkLocalizacao
            ));

            if (Boolean.FALSE.equals(loc.getActivo()))
            {
                throw new IllegalStateException("Localização está inactiva: " + loc.getPkLocalizacao());
            }
        }

        // 1) gerar movimentações (S) + atualizar snapshot (global + por armazém)
        LocalDateTime agora = LocalDateTime.now();

        for (VendaItem it : itens)
        {
            String codigo = it.getPortfolio().getPkPortfolio();

            // 1.1) atualizar snapshot com validação (se não houver stock, lança exceção e faz rollback)
            stockService.aplicarDelta(codigo, fkArmazem, -it.getQuantidade());

            // 1.2) salvar histórico
            MovimentacaoInventario mov = new MovimentacaoInventario();
            mov.setPortfolio(it.getPortfolio());
            mov.setTipoMovimento("S");
            mov.setQuantidade(it.getQuantidade());
            mov.setDataMovimento(agora);
            mov.setArmazem(armazem);
            mov.setLocalizacao(loc);
            movRepository.save(mov);
        }

        // 3) fechar venda
        v.setEstado("CONCLUIDA");
        v = vendaRepository.save(v);

        Venda vv = vendaRepository.buscarComCliente(v.getPkVenda());
        return toRTO(vv);
    }

    private VendaResponseRTO toRTO(Venda v)
    {
        Integer fkCliente = (v.getCliente() == null) ? null : v.getCliente().getPkPessoa();
        String nomeCliente = (v.getCliente() == null) ? null : v.getCliente().getNome();

        return new VendaResponseRTO(
            v.getPkVenda(),
            v.getDataVenda(),
            v.getEstado(),
            fkCliente,
            nomeCliente
        );
    }

    private String trim(String s)
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

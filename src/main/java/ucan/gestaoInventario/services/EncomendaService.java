package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ucan.gestaoInventario.dto.rto.EncomendaCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.EncomendaReceberDTO;
import ucan.gestaoInventario.dto.rto.EncomendaResponseRTO;
import ucan.gestaoInventario.entities.*;

import ucan.gestaoInventario.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EncomendaService
{

    private final EncomendaRepository encomendaRepository;
    private final PessoaRepository pessoaRepository;
    private final EncomendaItemRepository itemRepository;

    private final MovimentacaoInventarioRepository movRepo;
    private final ArmazemRepository armazemRepo;
    private final LocalizacaoRepository localizacaoRepo;

    private final StockService stockService;

    public EncomendaService(
        EncomendaRepository encomendaRepository,
        PessoaRepository pessoaRepository,
        EncomendaItemRepository itemRepository,
        MovimentacaoInventarioRepository movRepo,
        ArmazemRepository armazemRepo,
        LocalizacaoRepository localizacaoRepo,
        StockService stockService
    )
    {
        this.encomendaRepository = encomendaRepository;
        this.pessoaRepository = pessoaRepository;
        this.itemRepository = itemRepository;
        this.movRepo = movRepo;
        this.armazemRepo = armazemRepo;
        this.localizacaoRepo = localizacaoRepo;
        this.stockService = stockService;
    }

    // =========================
    // CRIAR ENCOMENDA (ABERTA)
    // =========================
    @Transactional
    public EncomendaResponseRTO criar(EncomendaCreateRequestDTO dto)
    {
        if (dto == null || dto.getFkFornecedor() == null)
        {
            throw new IllegalArgumentException("fkFornecedor é obrigatório");
        }

        Pessoa fornecedor = pessoaRepository.findById(dto.getFkFornecedor())
            .orElseThrow(() -> new IllegalStateException("Fornecedor não encontrado: " + dto.getFkFornecedor()));

        if (Boolean.FALSE.equals(fornecedor.getActivo()))
        {
            throw new IllegalStateException("Fornecedor está inactivo: " + fornecedor.getPkPessoa());
        }

        Encomenda e = new Encomenda();
        e.setDataEncomenda(LocalDateTime.now());
        e.setEstado("ABERTA");
        e.setFornecedor(fornecedor);

        e = encomendaRepository.save(e);

        return toRTO(e);
    }

    // =========================
    // LISTAR
    // =========================
    @Transactional(readOnly = true)
    public List<EncomendaResponseRTO> listar()
    {
        return encomendaRepository.listarComFornecedor()
            .stream()
            .map(this::toRTO)
            .toList();
    }

    // =========================
    // DETALHAR
    // =========================
    @Transactional(readOnly = true)
    public EncomendaResponseRTO detalhar(Integer id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id é obrigatório");
        }

        Encomenda e = encomendaRepository.buscarComFornecedor(id);
        if (e == null)
        {
            throw new IllegalStateException("Encomenda não encontrada: " + id);
        }

        return toRTO(e);
    }

    // =========================
    // RECEBER ENCOMENDA
    // - gera movimentos E
    // - atualiza snapshot produto.quantidadeExata (sobe)
    // - fecha encomenda como RECEBIDA
    // =========================
    @Transactional
    public EncomendaResponseRTO receber(Integer id, EncomendaReceberDTO dto)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id é obrigatório");
        }
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }
        if (dto.getFkArmazem() == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório");
        }

        Encomenda e = encomendaRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Encomenda não encontrada: " + id));

        if (!"ABERTA".equalsIgnoreCase(e.getEstado()))
        {
            throw new IllegalStateException("Só é possível receber encomenda com estado ABERTA");
        }

        // 1) buscar itens
        List<EncomendaItem> itens = itemRepository.listarItensDaEncomenda(id);
        if (itens == null || itens.isEmpty())
        {
            throw new IllegalStateException("Não é possível receber encomenda sem itens.");
        }

        // 2) validar armazém
        Armazem armazem = armazemRepo.findById(dto.getFkArmazem())
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + dto.getFkArmazem()));

        if (Boolean.FALSE.equals(armazem.getActivo()))
        {
            throw new IllegalStateException("Armazém está inactivo: " + armazem.getPkArmazem());
        }

        // 3) validar localização (se vier)
        Localizacao loc = null;
        if (dto.getFkLocalizacao() != null)
        {
            loc = localizacaoRepo
                .findByArmazem_PkArmazemAndPkLocalizacao(dto.getFkArmazem(), dto.getFkLocalizacao())
                .orElseThrow(() -> new IllegalStateException(
                "Localização não encontrada ou não pertence ao armazém. fkArmazem="
                + dto.getFkArmazem() + " fkLocalizacao=" + dto.getFkLocalizacao()
            ));

            if (Boolean.FALSE.equals(loc.getActivo()))
            {
                throw new IllegalStateException("Localização está inactiva: " + loc.getPkLocalizacao());
            }
        }

        // 4) gerar entradas (movimentos E) + atualizar snapshot
        LocalDateTime agora = LocalDateTime.now();
        List<MovimentacaoInventario> movs = new ArrayList<>();

        for (EncomendaItem it : itens)
        {
            String codigo = it.getPortfolio().getPkPortfolio();

            // 4.1) atualizar snapshots (global + por armazém)
            stockService.aplicarDelta(codigo, dto.getFkArmazem(), it.getQuantidade());

            // 4.2) criar movimentação
            MovimentacaoInventario mov = new MovimentacaoInventario();
            mov.setPortfolio(it.getPortfolio());
            mov.setTipoMovimento("E");
            mov.setQuantidade(it.getQuantidade());
            mov.setDataMovimento(agora);
            mov.setArmazem(armazem);
            mov.setLocalizacao(loc);

            movs.add(mov);
        }

        movRepo.saveAll(movs);

        // 5) fechar encomenda (persistir)
        e.setEstado("RECEBIDA");
        e = encomendaRepository.save(e);

        // se quiser, pode buscar com fornecedor aqui também (opcional)
        return toRTO(e);
    }

    // =========================
    // Mapper
    // =========================
    private EncomendaResponseRTO toRTO(Encomenda e)
    {
        Integer fkFornecedor = (e.getFornecedor() == null) ? null : e.getFornecedor().getPkPessoa();
        String nomeFornecedor = (e.getFornecedor() == null) ? null : e.getFornecedor().getNome();

        return new EncomendaResponseRTO(
            e.getPkEncomenda(),
            e.getDataEncomenda(),
            e.getEstado(),
            fkFornecedor,
            nomeFornecedor
        );
    }
}

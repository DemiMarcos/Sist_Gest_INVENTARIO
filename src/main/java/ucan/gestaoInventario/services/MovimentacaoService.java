package ucan.gestaoInventario.services;

import ucan.gestaoInventario.dto.rto.MovimentacaoRequestDTO;
import ucan.gestaoInventario.dto.rto.MovimentacaoResponseRTO;
import ucan.gestaoInventario.entities.Armazem;
import ucan.gestaoInventario.entities.Localizacao;
import ucan.gestaoInventario.entities.MovimentacaoInventario;
import ucan.gestaoInventario.entities.Portfolio;
import ucan.gestaoInventario.repositories.ArmazemRepository;
import ucan.gestaoInventario.repositories.LocalizacaoRepository;
import ucan.gestaoInventario.repositories.MovimentacaoInventarioRepository;
import ucan.gestaoInventario.repositories.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService
{

    private final MovimentacaoInventarioRepository movRepo;
    private final PortfolioRepository portfolioRepo;
    private final ArmazemRepository armazemRepo;
    private final LocalizacaoRepository localizacaoRepo;
    private final StockService stockService;

    public MovimentacaoService(
        MovimentacaoInventarioRepository movRepo,
        PortfolioRepository portfolioRepo,
        ArmazemRepository armazemRepo,
        LocalizacaoRepository localizacaoRepo,
        StockService stockService
    )
    {
        this.movRepo = movRepo;
        this.portfolioRepo = portfolioRepo;
        this.armazemRepo = armazemRepo;
        this.localizacaoRepo = localizacaoRepo;
        this.stockService = stockService;
    }

    @Transactional
    public MovimentacaoResponseRTO registarMovimento(MovimentacaoRequestDTO dto)
    {
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }

        if (dto.getFkPortfolio() == null || dto.getFkPortfolio().isBlank())
        {
            throw new IllegalArgumentException("fkPortfolio é obrigatório");
        }

        if (dto.getFkArmazem() == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório");
        }

        if (dto.getTipoMovimento() == null || dto.getTipoMovimento().isBlank())
        {
            throw new IllegalArgumentException("tipoMovimento é obrigatório");
        }

        if (!"E".equalsIgnoreCase(dto.getTipoMovimento()) && !"S".equalsIgnoreCase(dto.getTipoMovimento()))
        {
            throw new IllegalArgumentException("tipoMovimento deve ser 'E' ou 'S'");
        }

        if (dto.getQuantidade() == null || dto.getQuantidade() < 1)
        {
            throw new IllegalArgumentException("quantidade deve ser >= 1");
        }

        // 1) validar se portfolio existe
        Portfolio p = portfolioRepo.findById(dto.getFkPortfolio())
            .orElseThrow(() -> new IllegalStateException(
            "Portfólio não encontrado: " + dto.getFkPortfolio()
        ));

        if (dto.getFkArmazem() == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório");
        }

        // 2) validar armazém existe e está activo
        Armazem armazem = armazemRepo.findById(dto.getFkArmazem())
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + dto.getFkArmazem()));

        if (Boolean.FALSE.equals(armazem.getActivo()))
        {
            throw new IllegalStateException("Armazém está inactivo: " + armazem.getPkArmazem());
        }

        // 4) validar localização (se vier)
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

        // 4.1) aplicar delta no snapshot (GLOBAL + POR ARMAZÉM)
        // Entrada = +qtd | Saída = -qtd
        int delta = "E".equalsIgnoreCase(dto.getTipoMovimento())
            ? dto.getQuantidade()
            : -dto.getQuantidade();

        stockService.aplicarDelta(dto.getFkPortfolio(), dto.getFkArmazem(), delta);

        // 6) montar entidade de movimentação
        MovimentacaoInventario mov = new MovimentacaoInventario();
        mov.setPortfolio(p);
        mov.setTipoMovimento(dto.getTipoMovimento().toUpperCase());
        mov.setQuantidade(dto.getQuantidade());
        mov.setDataMovimento(LocalDateTime.now());
        mov.setArmazem(armazem);
        mov.setLocalizacao(loc);

        // 7) persistir movimentação
        MovimentacaoInventario saved = movRepo.save(mov);

        // 8) retornar
        return toRTO(saved);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponseRTO> listarMovimentos(String fkPortfolio)
    {
        List<MovimentacaoInventario> lista;

        if (fkPortfolio == null || fkPortfolio.isBlank())
        {
            lista = movRepo.findAllByOrderByDataMovimentoDesc();
        }
        else
        {
            if (!portfolioRepo.existsById(fkPortfolio))
            {
                throw new IllegalStateException("Portfólio não encontrado: " + fkPortfolio);
            }
            lista = movRepo.findByPortfolio_PkPortfolioOrderByDataMovimentoDesc(fkPortfolio);
        }

        return lista.stream().map(this::toRTO).toList();
    }

    private MovimentacaoResponseRTO toRTO(MovimentacaoInventario mov)
    {
        return new MovimentacaoResponseRTO(
            mov.getPkMovimentacao(),
            mov.getPortfolio().getPkPortfolio(),
            mov.getTipoMovimento(),
            mov.getQuantidade(),
            mov.getDataMovimento()
        );
    }
}

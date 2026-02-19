package ucan.gestaoInventario.services.avancadas;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ucan.gestaoInventario.dto.rto.avancadas.MovimentoSaldoAcumuladoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoABCRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoCompradoJuntoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoSemMovimentoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.RupturaIminenteRTO;
import ucan.gestaoInventario.repositories.avancadas.ConsultaInventarioAvancadaRepository;

@Service
public class ConsultaInventarioAvancadaService
{

    private final ConsultaInventarioAvancadaRepository repo;

    public ConsultaInventarioAvancadaService(ConsultaInventarioAvancadaRepository repo)
    {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<MovimentoSaldoAcumuladoRTO> auditoriaSaldoAcumulado(
        String fkPortfolio,
        Integer fkArmazem,
        LocalDateTime dtInicio,
        LocalDateTime dtFim
    )
    {
        if (fkPortfolio == null || fkPortfolio.isBlank())
        {
            throw new IllegalArgumentException("fkPortfolio é obrigatório.");
        }
        if (fkArmazem == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório.");
        }
        if (dtInicio == null || dtFim == null)
        {
            throw new IllegalArgumentException("dtInicio e dtFim são obrigatórios.");
        }
        if (dtFim.isBefore(dtInicio))
        {
            throw new IllegalArgumentException("dtFim não pode ser anterior a dtInicio.");
        }
        return repo.buscarMovimentosComSaldoAcumulado(fkPortfolio, fkArmazem, dtInicio, dtFim);
    }

    @Transactional(readOnly = true)
    public List<ProdutoABCRTO> curvaABC(LocalDateTime dtInicio, LocalDateTime dtFim)
    {
        if (dtInicio == null || dtFim == null)
        {
            throw new IllegalArgumentException("dtInicio e dtFim são obrigatórios.");
        }
        if (dtFim.isBefore(dtInicio))
        {
            throw new IllegalArgumentException("dtFim não pode ser anterior a dtInicio.");
        }
        return repo.buscarCurvaABC(dtInicio, dtFim);
    }

    @Transactional(readOnly = true)
    public List<ProdutoSemMovimentoRTO> produtosSemMovimento(Integer dias, String prefixo)
    {
        if (dias == null || dias <= 0)
        {
            throw new IllegalArgumentException("dias deve ser maior que 0.");
        }
        return repo.buscarProdutosSemMovimento(dias, prefixo);
    }

    @Transactional(readOnly = true)
    public List<RupturaIminenteRTO> rupturaIminente(Integer fkArmazem, Integer diasHistorico, Integer diasPrevistos)
    {
        if (fkArmazem == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório.");
        }
        if (diasHistorico == null || diasHistorico <= 0)
        {
            throw new IllegalArgumentException("diasHistorico inválido.");
        }
        if (diasPrevistos == null || diasPrevistos <= 0)
        {
            throw new IllegalArgumentException("diasPrevistos inválido.");
        }
        return repo.buscarRupturaIminente(fkArmazem, diasHistorico, diasPrevistos);
    }

    @Transactional(readOnly = true)
    public List<ProdutoCompradoJuntoRTO> produtosCompradosJuntos(
        String fkPortfolioBase,
        LocalDateTime dtInicio,
        LocalDateTime dtFim,
        Integer top
    )
    {
        if (fkPortfolioBase == null || fkPortfolioBase.isBlank())
        {
            throw new IllegalArgumentException("fkPortfolioBase é obrigatório.");
        }
        if (dtInicio == null || dtFim == null)
        {
            throw new IllegalArgumentException("dtInicio e dtFim são obrigatórios.");
        }
        if (dtFim.isBefore(dtInicio))
        {
            throw new IllegalArgumentException("dtFim não pode ser anterior a dtInicio.");
        }
        if (top == null || top <= 0)
        {
            top = 10;
        }
        return repo.buscarProdutosCompradosJuntos(fkPortfolioBase, dtInicio, dtFim, top);
    }
}

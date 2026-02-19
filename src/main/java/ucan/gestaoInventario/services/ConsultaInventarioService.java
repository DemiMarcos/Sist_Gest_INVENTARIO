package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucan.gestaoInventario.dto.rto.*;
import ucan.gestaoInventario.repositories.ConsultaInventarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaInventarioService
{

    private final ConsultaInventarioRepository repo;

    public ConsultaInventarioService(ConsultaInventarioRepository repo)
    {
        this.repo = repo;
    }

    // =========================
    // CONSULTA 1 (Enunciado #13)
    // =========================
    @Transactional(readOnly = true)
    public List<ProdutoStockPrecoNaDataRTO> listarStockEPrecoNaData(ProdutoStockPrecoNaDataRequestDTO dto)
    {
        if (dto.getFkArmazem() == null)
        {
            return repo.stockPrecoNaData_Global(
                dto.getFkPortfolios(),
                dto.getDataRef(),
                dto.getPrecoMin()
            );
        }

        return repo.stockPrecoNaData_PorArmazem(
            dto.getFkPortfolios(),
            dto.getDataRef(),
            dto.getPrecoMin(),
            dto.getFkArmazem()
        );
    }

    // =========================
    // CONSULTA 2
    // =========================
    @Transactional(readOnly = true)
    public List<RankingClienteValorRTO> rankingClientesPorValor(RankingClientesRequestDTO dto)
    {
        if (dto.getFkPortfolios() == null || dto.getFkPortfolios().isEmpty())
        {
            return repo.rankingClientes_Total(dto.getIni(), dto.getFim());
        }

        return repo.rankingClientes_FiltradoPorProdutos(dto.getIni(), dto.getFim(), dto.getFkPortfolios());
    }

    // =========================
    // CONSULTA 3
    // =========================
    @Transactional(readOnly = true)
    public List<ProdutoCriticoPorArmazemRTO> listarCriticosPorArmazem(Integer fkArmazem, String prefixo)
    {
        prefixo = trim(prefixo);

        if (prefixo == null)
        {
            return repo.criticosPorArmazem(fkArmazem);
        }

        return repo.criticosPorArmazemEPrefixo(fkArmazem, prefixo);
    }

    // =========================
    // CONSULTA 4
    // =========================
    @Transactional(readOnly = true)
    public List<MovimentacaoAuditoriaRTO> auditoriaMovimentos(
        LocalDateTime ini,
        LocalDateTime fim,
        Integer fkArmazem,
        Integer fkLocalizacao,
        String fkPortfolio
    )
    {
        fkPortfolio = trim(fkPortfolio);

        if (fkLocalizacao == null && fkPortfolio == null)
        {
            return repo.auditoriaPorArmazem(ini, fim, fkArmazem);
        }

        if (fkLocalizacao != null && fkPortfolio == null)
        {
            return repo.auditoriaPorArmazemELocalizacao(ini, fim, fkArmazem, fkLocalizacao);
        }

        if (fkLocalizacao == null)
        {
            return repo.auditoriaPorArmazemEProduto(ini, fim, fkArmazem, fkPortfolio);
        }

        return repo.auditoriaCompleta(ini, fim, fkArmazem, fkLocalizacao, fkPortfolio);
    }

    // =========================
    // CONSULTA 5
    // =========================
    @Transactional(readOnly = true)
    public List<FornecedorProdutoNoPeriodoRTO> fornecedoresProdutosNoPeriodo(
        String sufixoNome,
        LocalDateTime ini,
        LocalDateTime fim,
        String prefixo
    )
    {
        sufixoNome = trim(sufixoNome);
        if (sufixoNome == null)
        {
            sufixoNome = ""; // aceitar vazio
        }
        prefixo = trim(prefixo);

        if (prefixo == null)
        {
            return repo.fornecedoresProdutosNoPeriodo(sufixoNome, ini, fim);
        }

        return repo.fornecedoresProdutosNoPeriodoEPrefixo(sufixoNome, ini, fim, prefixo);
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
}

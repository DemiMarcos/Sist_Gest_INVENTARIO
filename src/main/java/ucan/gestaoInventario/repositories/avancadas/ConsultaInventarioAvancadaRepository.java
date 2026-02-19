package ucan.gestaoInventario.repositories.avancadas;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.dto.rto.avancadas.MovimentoSaldoAcumuladoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoABCRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoCompradoJuntoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoSemMovimentoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.RupturaIminenteRTO;
import ucan.gestaoInventario.entities.MovimentacaoInventario;

public interface ConsultaInventarioAvancadaRepository extends Repository<MovimentacaoInventario, Integer>
{

    // (1) Auditoria com saldo acumulado (WINDOW FUNCTION)
    @Query(value = """
        SELECT
            mi.pk_movimentacao AS idMov,
            mi.data_movimento  AS dataMov,
            mi.tipo_movimento  AS tipo,
            mi.quantidade      AS quantidade,
            SUM(CASE WHEN mi.tipo_movimento = 'E' THEN mi.quantidade ELSE -mi.quantidade END)
              OVER (ORDER BY mi.data_movimento, mi.pk_movimentacao) AS saldoAcumulado
        FROM movimentacao_inventario mi
        WHERE mi.fk_portfolio = :fkPortfolio
          AND mi.fk_armazem = :fkArmazem
          AND mi.data_movimento BETWEEN :dtInicio AND :dtFim
        ORDER BY mi.data_movimento ASC, mi.pk_movimentacao ASC
        """, nativeQuery = true)
    List<MovimentoSaldoAcumuladoRTO> buscarMovimentosComSaldoAcumulado(
        @Param("fkPortfolio") String fkPortfolio,
        @Param("fkArmazem") Integer fkArmazem,
        @Param("dtInicio") LocalDateTime dtInicio,
        @Param("dtFim") LocalDateTime dtFim
    );

    // (2) Curva ABC por valor de vendas (CTE + WINDOW)
    @Query(value = """
        WITH base AS (
          SELECT vi.fk_portfolio,
                 SUM(vi.quantidade * vi.preco_unitario) AS valor
          FROM venda_item vi
          JOIN venda v ON v.pk_venda = vi.fk_venda
          WHERE v.estado = 'CONCLUIDA'
            AND v.data_venda BETWEEN :dtInicio AND :dtFim
          GROUP BY vi.fk_portfolio
        ),
        ord AS (
          SELECT b.*,
                 SUM(valor) OVER () AS total,
                 SUM(valor) OVER (ORDER BY valor DESC) AS acumulado
          FROM base b
        )
        SELECT
            o.fk_portfolio AS fkPortfolio,
            pf.descricao   AS descricao,
            o.valor        AS valorVendido,
            (o.acumulado / NULLIF(o.total, 0)) * 100 AS percAcumulado,
            CASE
              WHEN (o.acumulado / NULLIF(o.total, 0)) <= 0.80 THEN 'A'
              WHEN (o.acumulado / NULLIF(o.total, 0)) <= 0.95 THEN 'B'
              ELSE 'C'
            END AS classe
        FROM ord o
        JOIN portfolio pf ON pf.pk_portfolio = o.fk_portfolio
        ORDER BY o.valor DESC, pf.descricao ASC
        """, nativeQuery = true)
    List<ProdutoABCRTO> buscarCurvaABC(
        @Param("dtInicio") LocalDateTime dtInicio,
        @Param("dtFim") LocalDateTime dtFim
    );

    // (3) Produtos sem movimento há X dias (LEFT JOIN + HAVING + COALESCE)
    @Query(value = """
        SELECT p.fk_portfolio AS fkPortfolio,
               pf.descricao   AS descricao,
               MAX(mi.data_movimento) AS ultimaMovimentacao
        FROM produto p
        JOIN portfolio pf ON pf.pk_portfolio = p.fk_portfolio
        LEFT JOIN movimentacao_inventario mi
               ON mi.fk_portfolio = p.fk_portfolio
        GROUP BY p.fk_portfolio, pf.descricao
        HAVING COALESCE(MAX(mi.data_movimento), TIMESTAMP '1900-01-01')
               < (NOW() - (CAST(:dias AS INT) || ' days')::interval)
           AND (:prefixo IS NULL OR p.fk_portfolio LIKE CONCAT(:prefixo, '%'))
        ORDER BY pf.descricao ASC
        """, nativeQuery = true)
    List<ProdutoSemMovimentoRTO> buscarProdutosSemMovimento(
        @Param("dias") Integer dias,
        @Param("prefixo") String prefixo
    );

    // (4) Ruptura iminente (CTE + previsão por média)
    @Query(value = """
        WITH saidas AS (
          SELECT mi.fk_portfolio,
                 SUM(mi.quantidade)::int AS qtdSaidaPeriodo
          FROM movimentacao_inventario mi
          WHERE mi.tipo_movimento = 'S'
            AND mi.fk_armazem = :fkArmazem
            AND mi.data_movimento >= (NOW() - (CAST(:diasHistorico AS INT) || ' days')::interval)
          GROUP BY mi.fk_portfolio
        )
        SELECT ps.fk_portfolio AS fkPortfolio,
               pf.descricao    AS descricao,
               ps.quantidade_exata AS stockAtual,
               COALESCE(sa.qtdSaidaPeriodo,0) AS saidaPeriodo,
               (COALESCE(sa.qtdSaidaPeriodo,0)::double precision / :diasHistorico) AS mediaSaidaDia,
               (:diasPrevistos * (COALESCE(sa.qtdSaidaPeriodo,0)::double precision / :diasHistorico)) AS consumoPrevisto
        FROM produto_stock ps
        JOIN portfolio pf ON pf.pk_portfolio = ps.fk_portfolio
        LEFT JOIN saidas sa ON sa.fk_portfolio = ps.fk_portfolio
        WHERE ps.fk_armazem = :fkArmazem
          AND ps.quantidade_exata < (:diasPrevistos * (COALESCE(sa.qtdSaidaPeriodo,0)::double precision / :diasHistorico))
        ORDER BY consumoPrevisto DESC, pf.descricao ASC
        """, nativeQuery = true)
    List<RupturaIminenteRTO> buscarRupturaIminente(
        @Param("fkArmazem") Integer fkArmazem,
        @Param("diasHistorico") Integer diasHistorico,
        @Param("diasPrevistos") Integer diasPrevistos
    );

    // (5) Produtos comprados juntos (CTE + associação por venda)
    @Query(value = """
        WITH vendas_base AS (
          SELECT DISTINCT vi.fk_venda
          FROM venda_item vi
          JOIN venda v ON v.pk_venda = vi.fk_venda
          WHERE vi.fk_portfolio = :fkPortfolioBase
            AND v.estado = 'CONCLUIDA'
            AND v.data_venda BETWEEN :dtInicio AND :dtFim
        )
        SELECT
            vi.fk_portfolio AS fkPortfolio,
            pf.descricao    AS descricao,
            COUNT(*)::int   AS frequencia
        FROM venda_item vi
        JOIN vendas_base vb ON vb.fk_venda = vi.fk_venda
        JOIN portfolio pf ON pf.pk_portfolio = vi.fk_portfolio
        WHERE vi.fk_portfolio <> :fkPortfolioBase
        GROUP BY vi.fk_portfolio, pf.descricao
        ORDER BY COUNT(*) DESC, pf.descricao ASC
        LIMIT :top
        """, nativeQuery = true)
    List<ProdutoCompradoJuntoRTO> buscarProdutosCompradosJuntos(
        @Param("fkPortfolioBase") String fkPortfolioBase,
        @Param("dtInicio") LocalDateTime dtInicio,
        @Param("dtFim") LocalDateTime dtFim,
        @Param("top") Integer top
    );
}

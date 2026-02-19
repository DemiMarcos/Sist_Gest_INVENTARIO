package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.dto.rto.*;
import ucan.gestaoInventario.entities.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaInventarioRepository extends JpaRepository<Produto, String>
{
    // ======================================================
    // CONSULTA 1 (Enunciado #13)
    // Preço na data (produto_preco) + stock (global OU por armazém)
    // ======================================================

    // 1A) GLOBAL: stock vem de produto.quantidadeExata
    @Query("""
        SELECT new ucan.gestaoInventario.dto.rto.ProdutoStockPrecoNaDataRTO(
            p.fkPortfolio,
            pf.descricao,
            pp.preco,
            p.quantidadeExata,
            :dataRef
        )
        FROM Produto p
        JOIN p.portfolio pf
        JOIN ProdutoPreco pp
          ON pp.produto = p
         AND pp.dataInicio <= :dataRef
         AND (pp.dataFim IS NULL OR pp.dataFim > :dataRef)
        WHERE p.fkPortfolio IN :fks
          AND pp.preco > :precoMin
        ORDER BY pf.descricao ASC
    """)
    List<ProdutoStockPrecoNaDataRTO> stockPrecoNaData_Global(
        @Param("fks") List<String> fks,
        @Param("dataRef") LocalDateTime dataRef,
        @Param("precoMin") Double precoMin
    );

    // 1B) POR ARMAZÉM: stock vem de produto_stock.quantidadeExata
    @Query("""
        SELECT new ucan.gestaoInventario.dto.rto.ProdutoStockPrecoNaDataRTO(
            p.fkPortfolio,
            pf.descricao,
            pp.preco,
            COALESCE(ps.quantidadeExata, 0),
            :dataRef
        )
        FROM Produto p
        JOIN p.portfolio pf
        JOIN ProdutoPreco pp
          ON pp.produto = p
         AND pp.dataInicio <= :dataRef
         AND (pp.dataFim IS NULL OR pp.dataFim > :dataRef)
        LEFT JOIN ProdutoStock ps
          ON ps.fkPortfolio = p.fkPortfolio
         AND ps.fkArmazem  = :fkArmazem
        WHERE p.fkPortfolio IN :fks
          AND pp.preco > :precoMin
        ORDER BY pf.descricao ASC
    """)
    List<ProdutoStockPrecoNaDataRTO> stockPrecoNaData_PorArmazem(
        @Param("fks") List<String> fks,
        @Param("dataRef") LocalDateTime dataRef,
        @Param("precoMin") Double precoMin,
        @Param("fkArmazem") Integer fkArmazem
    );

    // ===========================================================
    // CONSULTA 2
    // Ranking de clientes por valor total de compras no período
    // ===========================================================
    @Query("""
       SELECT new ucan.gestaoInventario.dto.rto.RankingClienteValorRTO(
            c.pkPessoa,
            c.nome,
            COALESCE(SUM(vi.precoUnitario * vi.quantidade), 0.0)
       )
       FROM VendaItem vi
       JOIN vi.venda v
       JOIN v.cliente c
       WHERE v.dataVenda BETWEEN :ini AND :fim
         AND UPPER(v.estado) = 'CONCLUIDA'
       GROUP BY c.pkPessoa, c.nome
       ORDER BY COALESCE(SUM(vi.precoUnitario * vi.quantidade), 0.0) DESC
    """)
    List<RankingClienteValorRTO> rankingClientes_Total(
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim
    );

    @Query("""
       SELECT new ucan.gestaoInventario.dto.rto.RankingClienteValorRTO(
            c.pkPessoa,
            c.nome,
            COALESCE(SUM(vi.precoUnitario * vi.quantidade), 0.0)
       )
       FROM VendaItem vi
       JOIN vi.venda v
       JOIN v.cliente c
       WHERE v.dataVenda BETWEEN :ini AND :fim
         AND UPPER(v.estado) = 'CONCLUIDA'
         AND vi.portfolio.pkPortfolio IN :fks
       GROUP BY c.pkPessoa, c.nome
       ORDER BY COALESCE(SUM(vi.precoUnitario * vi.quantidade), 0.0) DESC
    """)
    List<RankingClienteValorRTO> rankingClientes_FiltradoPorProdutos(
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim,
        @Param("fks") List<String> fks
    );

    // ======================================================
    // CONSULTA 3
    // Produtos críticos por armazém: produto_stock < produto.quantidadeCritica
    // ======================================================
    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.ProdutoCriticoPorArmazemRTO(
          :fkArmazem,
          p.fkPortfolio,
          pf.descricao,
          COALESCE(ps.quantidadeExata, 0),
          COALESCE(p.quantidadeCritica, 0)
      )
      FROM Produto p
      JOIN p.portfolio pf
      LEFT JOIN ProdutoStock ps
        ON ps.fkPortfolio = p.fkPortfolio
       AND ps.fkArmazem  = :fkArmazem
      WHERE COALESCE(ps.quantidadeExata, 0) < COALESCE(p.quantidadeCritica, 0)
      ORDER BY pf.descricao ASC
    """)
    List<ProdutoCriticoPorArmazemRTO> criticosPorArmazem(@Param("fkArmazem") Integer fkArmazem);

    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.ProdutoCriticoPorArmazemRTO(
          :fkArmazem,
          p.fkPortfolio,
          pf.descricao,
          COALESCE(ps.quantidadeExata, 0),
          COALESCE(p.quantidadeCritica, 0)
      )
      FROM Produto p
      JOIN p.portfolio pf
      LEFT JOIN ProdutoStock ps
        ON ps.fkPortfolio = p.fkPortfolio
       AND ps.fkArmazem  = :fkArmazem
      WHERE p.fkPortfolio LIKE CONCAT(:prefixo, '%')
        AND COALESCE(ps.quantidadeExata, 0) < COALESCE(p.quantidadeCritica, 0)
      ORDER BY pf.descricao ASC
    """)
    List<ProdutoCriticoPorArmazemRTO> criticosPorArmazemEPrefixo(
        @Param("fkArmazem") Integer fkArmazem,
        @Param("prefixo") String prefixo
    );

    // ======================================================================
    // CONSULTA 4
    // Auditoria de movimentos (intervalo + armazém + localização + produto)
    // ======================================================================
    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.MovimentacaoAuditoriaRTO(
        m.pkMovimentacao,
        m.portfolio.pkPortfolio,
        m.tipoMovimento,
        m.quantidade,
        m.dataMovimento,
        m.armazem.pkArmazem,
        (CASE WHEN m.localizacao IS NULL THEN null ELSE m.localizacao.pkLocalizacao END)
      )
      FROM MovimentacaoInventario m
      WHERE m.dataMovimento BETWEEN :ini AND :fim
        AND m.armazem.pkArmazem = :fkArmazem
      ORDER BY m.dataMovimento ASC
    """)
    List<MovimentacaoAuditoriaRTO> auditoriaPorArmazem(
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim,
        @Param("fkArmazem") Integer fkArmazem
    );

    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.MovimentacaoAuditoriaRTO(
        m.pkMovimentacao,
        m.portfolio.pkPortfolio,
        m.tipoMovimento,
        m.quantidade,
        m.dataMovimento,
        m.armazem.pkArmazem,
        m.localizacao.pkLocalizacao
      )
      FROM MovimentacaoInventario m
      WHERE m.dataMovimento BETWEEN :ini AND :fim
        AND m.armazem.pkArmazem = :fkArmazem
        AND m.localizacao.pkLocalizacao = :fkLocalizacao
      ORDER BY m.dataMovimento ASC
    """)
    List<MovimentacaoAuditoriaRTO> auditoriaPorArmazemELocalizacao(
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim,
        @Param("fkArmazem") Integer fkArmazem,
        @Param("fkLocalizacao") Integer fkLocalizacao
    );

    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.MovimentacaoAuditoriaRTO(
        m.pkMovimentacao,
        m.portfolio.pkPortfolio,
        m.tipoMovimento,
        m.quantidade,
        m.dataMovimento,
        m.armazem.pkArmazem,
        (CASE WHEN m.localizacao IS NULL THEN null ELSE m.localizacao.pkLocalizacao END)
      )
      FROM MovimentacaoInventario m
      WHERE m.dataMovimento BETWEEN :ini AND :fim
        AND m.armazem.pkArmazem = :fkArmazem
        AND m.portfolio.pkPortfolio = :fkPortfolio
      ORDER BY m.dataMovimento ASC
    """)
    List<MovimentacaoAuditoriaRTO> auditoriaPorArmazemEProduto(
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim,
        @Param("fkArmazem") Integer fkArmazem,
        @Param("fkPortfolio") String fkPortfolio
    );

    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.MovimentacaoAuditoriaRTO(
        m.pkMovimentacao,
        m.portfolio.pkPortfolio,
        m.tipoMovimento,
        m.quantidade,
        m.dataMovimento,
        m.armazem.pkArmazem,
        m.localizacao.pkLocalizacao
      )
      FROM MovimentacaoInventario m
      WHERE m.dataMovimento BETWEEN :ini AND :fim
        AND m.armazem.pkArmazem = :fkArmazem
        AND m.localizacao.pkLocalizacao = :fkLocalizacao
        AND m.portfolio.pkPortfolio = :fkPortfolio
      ORDER BY m.dataMovimento ASC
    """)
    List<MovimentacaoAuditoriaRTO> auditoriaCompleta(
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim,
        @Param("fkArmazem") Integer fkArmazem,
        @Param("fkLocalizacao") Integer fkLocalizacao,
        @Param("fkPortfolio") String fkPortfolio
    );

    // ======================================================
    // CONSULTA 5
    // Fornecedores (nome termina em sufixo) + produtos recebidos no período
    // ======================================================
    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.FornecedorProdutoNoPeriodoRTO(
          f.pkPessoa,
          f.nome,
          pf.pkPortfolio,
          pf.descricao,
          COALESCE(SUM(ei.quantidade), 0)
      )
      FROM EncomendaItem ei
      JOIN ei.encomenda e
      JOIN e.fornecedor f
      JOIN ei.portfolio pf
      WHERE e.dataEncomenda BETWEEN :ini AND :fim
        AND UPPER(f.nome) LIKE CONCAT('%', UPPER(:sufixo))
      GROUP BY f.pkPessoa, f.nome, pf.pkPortfolio, pf.descricao
      ORDER BY f.nome ASC, pf.descricao ASC
    """)
    List<FornecedorProdutoNoPeriodoRTO> fornecedoresProdutosNoPeriodo(
        @Param("sufixo") String sufixo,
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim
    );

    @Query("""
      SELECT new ucan.gestaoInventario.dto.rto.FornecedorProdutoNoPeriodoRTO(
          f.pkPessoa,
          f.nome,
          pf.pkPortfolio,
          pf.descricao,
          COALESCE(SUM(ei.quantidade), 0)
      )
      FROM EncomendaItem ei
      JOIN ei.encomenda e
      JOIN e.fornecedor f
      JOIN ei.portfolio pf
      WHERE e.dataEncomenda BETWEEN :ini AND :fim
        AND UPPER(f.nome) LIKE CONCAT('%', UPPER(:sufixo))
        AND pf.pkPortfolio LIKE CONCAT(:prefixo, '%')
      GROUP BY f.pkPessoa, f.nome, pf.pkPortfolio, pf.descricao
      ORDER BY f.nome ASC, pf.descricao ASC
    """)
    List<FornecedorProdutoNoPeriodoRTO> fornecedoresProdutosNoPeriodoEPrefixo(
        @Param("sufixo") String sufixo,
        @Param("ini") LocalDateTime ini,
        @Param("fim") LocalDateTime fim,
        @Param("prefixo") String prefixo
    );
}

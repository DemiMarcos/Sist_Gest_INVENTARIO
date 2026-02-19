package ucan.gestaoInventario.repositories.defesa;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ucan.gestaoInventario.dto.rto.defesa.DefesaProdutoPrecoStockRTO;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DefesaRepository
{

    private final NamedParameterJdbcTemplate jdbc;

    public DefesaRepository(NamedParameterJdbcTemplate jdbc)
    {
        this.jdbc = jdbc;
    }

    /**
     * Lista produtos de um grupo (prefixo opcional) com: - stock no armazém -
     * preço válido na dataRef - preço >= precoMin Ordenado pela descrição do
     * portfólio (ordem alfabética).
     */
    public List<DefesaProdutoPrecoStockRTO> listarPrecoStockNaData(
        Integer fkArmazem,
        LocalDateTime dataRef,
        Double precoMin,
        String prefixo
    )
    {
        String sql = """
            SELECT
                p.fk_portfolio AS fk_portfolio,
                pf.descricao   AS descricao,
                pp.preco       AS preco,
                COALESCE(ps.stock, 0) AS stock
            FROM produto p
            JOIN portfolio pf
                ON pf.pk_portfolio = p.fk_portfolio
            LEFT JOIN produto_stock ps
                ON ps.fk_produto = p.pk_produto
               AND ps.fk_armazem = :fkArmazem
            LEFT JOIN produto_preco pp
                ON pp.fk_produto = p.pk_produto
               AND :dataRef >= pp.data_inicio
               AND (:dataRef <= pp.data_fim OR pp.data_fim IS NULL)
            WHERE 1=1
              AND (:precoMin IS NULL OR pp.preco >= :precoMin)
              AND (:prefixo IS NULL OR pf.pk_portfolio LIKE :prefixoLike)
            ORDER BY pf.descricao ASC
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("fkArmazem", fkArmazem)
            .addValue("dataRef", dataRef)
            .addValue("precoMin", precoMin)
            .addValue("prefixo", (prefixo == null || prefixo.isBlank()) ? null : prefixo.trim())
            .addValue("prefixoLike", (prefixo == null || prefixo.isBlank()) ? null : (prefixo.trim() + "%"));

        return jdbc.query(sql, params, (rs, rowNum)
            -> new DefesaProdutoPrecoStockRTO(
                rs.getString("fk_portfolio"),
                rs.getString("descricao"),
                rs.getObject("preco") == null ? null : rs.getDouble("preco"),
                rs.getInt("stock")
            )
        );
    }
}

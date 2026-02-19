package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.dto.rto.ProdutoStockPrecoNaDataRTO;
import ucan.gestaoInventario.entities.Produto;

import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioRepository extends Repository<Produto, String>
{

    @Query("""
        select new ucan.gestaoInventario.dto.rto.ProdutoStockPrecoNaDataRTO(
            pr.fkPortfolio,
            p.descricao,
            pp.preco,
            cast(coalesce(sum(
                case
                    when m.tipoMovimento = 'E' then m.quantidade
                    when m.tipoMovimento = 'S' then -m.quantidade
                    else 0
                end
            ), 0) as integer),
            :dataRef
        )
        from Produto pr
        join pr.portfolio p
        join ProdutoPreco pp
             on pp.produto = pr
        left join MovimentacaoInventario m
               on m.portfolio = p
              and m.dataMovimento <= :dataRef
        where (p.pkPortfolio = :prefixo or p.pkPortfolio like concat(:prefixo, '.%'))
          and pp.dataInicio <= :dataRef
          and (pp.dataFim is null or pp.dataFim > :dataRef)
          and pp.preco > :precoMin
          and pp.dataInicio = (
                select max(pp2.dataInicio)
                from ProdutoPreco pp2
                where pp2.produto = pr
                  and pp2.dataInicio <= :dataRef
                  and (pp2.dataFim is null or pp2.dataFim > :dataRef)
          )
        group by pr.fkPortfolio, p.descricao, pp.preco
        order by lower(p.descricao)
    """)
    List<ProdutoStockPrecoNaDataRTO> listarProdutosStockPrecoPorGrupoNaData(
        @Param("prefixo") String prefixo,
        @Param("dataRef") LocalDateTime dataRef,
        @Param("precoMin") Double precoMin
    );
}

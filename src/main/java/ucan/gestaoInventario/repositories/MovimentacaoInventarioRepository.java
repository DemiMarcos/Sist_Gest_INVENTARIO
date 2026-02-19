package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ucan.gestaoInventario.entities.MovimentacaoInventario;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoInventarioRepository extends JpaRepository<MovimentacaoInventario, Integer>
{

    // Lista tudo (ordenado do mais recente para o mais antigo)
    List<MovimentacaoInventario> findAllByOrderByDataMovimentoDesc();

    // Lista por fk_portfolio (ordenado do mais recente para o mais antigo)
    List<MovimentacaoInventario> findByPortfolio_PkPortfolioOrderByDataMovimentoDesc(String pkPortfolio);

    // Lista por armazém (ordenado do mais recente para o mais antigo)
    List<MovimentacaoInventario> findByArmazem_PkArmazemOrderByDataMovimentoDesc(Integer fkArmazem);

    // Lista por produto (portfolio) + armazém
    List<MovimentacaoInventario> findByPortfolio_PkPortfolioAndArmazem_PkArmazemOrderByDataMovimentoDesc(
        String pkPortfolio,
        Integer fkArmazem
    );

    // Calcula o stock atual GLOBAL (E soma, S subtrai)
    @Query("""
        select coalesce(sum(
            case
                when m.tipoMovimento = 'E' then m.quantidade
                when m.tipoMovimento = 'S' then -m.quantidade
                else 0
            end
        ), 0)
        from MovimentacaoInventario m
        where m.portfolio.pkPortfolio = :codigo
    """)
    Integer calcularStock(@Param("codigo") String codigo);

    // Calcula o stock POR ARMAZÉM
    @Query("""
        select coalesce(sum(
            case
                when m.tipoMovimento = 'E' then m.quantidade
                when m.tipoMovimento = 'S' then -m.quantidade
                else 0
            end
        ), 0)
        from MovimentacaoInventario m
        where m.portfolio.pkPortfolio = :codigo
          and m.armazem.pkArmazem = :fkArmazem
    """)
    Integer calcularStockPorArmazem(
        @Param("codigo") String codigo,
        @Param("fkArmazem") Integer fkArmazem
    );

    // (muito útil em relatórios): Movimentos por intervalo de datas (global)
    List<MovimentacaoInventario> findByDataMovimentoBetweenOrderByDataMovimentoDesc(
        LocalDateTime inicio,
        LocalDateTime fim
    );

    // Movimentos por intervalo de datas + armazém
    List<MovimentacaoInventario> findByArmazem_PkArmazemAndDataMovimentoBetweenOrderByDataMovimentoDesc(
        Integer fkArmazem,
        LocalDateTime inicio,
        LocalDateTime fim
    );
}

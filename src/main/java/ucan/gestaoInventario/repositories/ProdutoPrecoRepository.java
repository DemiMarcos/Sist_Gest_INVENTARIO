package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.ProdutoPreco;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProdutoPrecoRepository extends JpaRepository<ProdutoPreco, Integer>
{

    // Preço atual (dataFim = null)
    Optional<ProdutoPreco> findFirstByProduto_FkPortfolioAndDataFimIsNullOrderByDataInicioDesc(String codigo);

    // Histórico (mais recente primeiro)
    List<ProdutoPreco> findByProduto_FkPortfolioOrderByDataInicioDesc(String codigo);

    // FECHA o preço atual via UPDATE direto (resolve o teu erro do UNIQUE)
    @Modifying
    @Query("""
        update ProdutoPreco pp
        set pp.dataFim = :agora
        where pp.produto.fkPortfolio = :codigo
          and pp.dataFim is null
    """)
    int fecharPrecoAtual(@Param("codigo") String codigo, @Param("agora") LocalDateTime agora);
}

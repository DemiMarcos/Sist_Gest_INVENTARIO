package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, String>
{

    @Query("""
        select pr
        from Produto pr
        join pr.portfolio p
        where lower(p.descricao) like lower(concat('%', :nome, '%'))
        order by p.pkPortfolio
    """)
    List<Produto> pesquisarPorNome(@Param("nome") String nome);

    @Query("""
        select pr
        from Produto pr
        join fetch pr.portfolio p
        where p.pkPortfolio = :codigo
    """)
    Produto buscarComPortfolio(@Param("codigo") String codigo);

    // AGORA USA O SNAPSHOT (quantidadeExata)
    @Query("""
        select pr
        from Produto pr
        where pr.quantidadeExata <= pr.quantidadeCritica
        order by pr.fkPortfolio
    """)
    List<Produto> listarAbaixoDoCritico();

    // AGORA USA O SNAPSHOT (quantidadeExata)
    @Query("""
        select pr
        from Produto pr
        where pr.quantidadeExata >= pr.quantidadeMaxima
        order by pr.fkPortfolio
    """)
    List<Produto> listarAcimaDoMaximo();

    @Query("""
        select pr
        from Produto pr
        where pr.fkPortfolio like concat(:prefixo, '.%')
        order by pr.fkPortfolio
    """)
    List<Produto> listarPorPrefixo(@Param("prefixo") String prefixo);
}

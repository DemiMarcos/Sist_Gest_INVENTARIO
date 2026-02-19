package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.Encomenda;

import java.util.List;

public interface EncomendaRepository extends JpaRepository<Encomenda, Integer>
{

    boolean existsByFornecedor_PkPessoa(Integer pkPessoa);

    @Query("""
        select e
        from Encomenda e
        left join fetch e.fornecedor f
        order by e.pkEncomenda desc
    """)
    List<Encomenda> listarComFornecedor();

    @Query("""
        select e
        from Encomenda e
        left join fetch e.fornecedor f
        where e.pkEncomenda = :id
    """)
    Encomenda buscarComFornecedor(@Param("id") Integer id);
}

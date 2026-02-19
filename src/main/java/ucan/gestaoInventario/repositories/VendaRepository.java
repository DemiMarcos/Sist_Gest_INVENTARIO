package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.Venda;

import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Integer>
{

    boolean existsByCliente_PkPessoa(Integer pkPessoa);

    @Query("""
        select v
        from Venda v
        join fetch v.cliente c
        order by v.pkVenda desc
    """)
    List<Venda> listarComCliente();

    @Query("""
        select v
        from Venda v
        join fetch v.cliente c
        where v.pkVenda = :id
    """)
    Venda buscarComCliente(@Param("id") Integer id);
}

package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.VendaItem;

import java.util.List;

public interface VendaItemRepository extends JpaRepository<VendaItem, Integer>
{

    @Query("""
        select vi
        from VendaItem vi
        join fetch vi.venda v
        join fetch vi.portfolio p
        where v.pkVenda = :id
        order by vi.pkItem
    """)
    List<VendaItem> listarItensDaVenda(@Param("id") Integer id);

    boolean existsByVenda_PkVenda(Integer pkVenda);
}

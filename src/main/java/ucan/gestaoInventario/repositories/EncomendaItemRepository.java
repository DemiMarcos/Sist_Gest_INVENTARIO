package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ucan.gestaoInventario.entities.EncomendaItem;

import java.util.List;

public interface EncomendaItemRepository extends JpaRepository<EncomendaItem, Integer>
{

    @Query("""
        select ei
        from EncomendaItem ei
        join fetch ei.encomenda e
        join fetch ei.portfolio p
        where e.pkEncomenda = :id
        order by ei.pkItem
    """)
    List<EncomendaItem> listarItensDaEncomenda(@Param("id") Integer id);
}

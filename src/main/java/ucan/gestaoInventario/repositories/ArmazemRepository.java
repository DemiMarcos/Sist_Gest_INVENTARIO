package ucan.gestaoInventario.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ucan.gestaoInventario.entities.Armazem;

public interface ArmazemRepository extends JpaRepository<Armazem, Integer>
{

    Optional<Armazem> findByNomeIgnoreCase(String nome);
}

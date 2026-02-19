package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.gestaoInventario.entities.Utilizador;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Integer>
{

    boolean existsByUsernameIgnoreCase(String username);
}

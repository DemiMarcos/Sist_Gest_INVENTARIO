package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.gestaoInventario.entities.PessoaTipo;

public interface PessoaTipoRepository extends JpaRepository<PessoaTipo, String>
{
}

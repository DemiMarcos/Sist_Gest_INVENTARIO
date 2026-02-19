package ucan.gestaoInventario.repositories;

import ucan.gestaoInventario.entities.Versao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersaoRepository extends JpaRepository<Versao, String>
{
}

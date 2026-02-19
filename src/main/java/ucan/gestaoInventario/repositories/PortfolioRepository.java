package ucan.gestaoInventario.repositories;

import ucan.gestaoInventario.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, String>
{
}

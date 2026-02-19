package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucan.gestaoInventario.entities.ProdutoStock;
import ucan.gestaoInventario.entities.ProdutoStockArmazem;

public interface ProdutoStockRepository extends JpaRepository<ProdutoStock, ProdutoStockArmazem>
{
}

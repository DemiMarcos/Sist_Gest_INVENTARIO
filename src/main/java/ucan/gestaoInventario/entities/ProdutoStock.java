package ucan.gestaoInventario.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Snapshot de stock por armazém.
 *
 * - produto.quantidade_exata   => saldo GLOBAL (tudo)
 * - produto_stock.quantidade_exata => saldo POR ARMAZÉM
 */
@Entity
@Table(name = "produto_stock")
@IdClass(ProdutoStockArmazem.class)
@Getter
@Setter
@NoArgsConstructor
public class ProdutoStock
{

    @Id
    @Column(name = "fk_portfolio", length = 50, nullable = false)
    private String fkPortfolio;

    @Id
    @Column(name = "fk_armazem", nullable = false)
    private Integer fkArmazem;

    @Column(name = "quantidade_exata", nullable = false)
    private Integer quantidadeExata = 0;
}

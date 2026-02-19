package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portfolio")
@Getter
@Setter
@NoArgsConstructor
public class Portfolio
{

    @Id
    @Column(name = "pk_portfolio", length = 50)
    private String pkPortfolio;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    // raiz pode não ter pai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_portfolio_pai", nullable = true)
    private Portfolio fkPortfolioPai;
}

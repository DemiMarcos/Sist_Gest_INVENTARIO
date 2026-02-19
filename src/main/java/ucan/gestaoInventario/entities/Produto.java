package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
public class Produto
{

    @Id
    @Column(name = "fk_portfolio", length = 50, nullable = false)
    private String fkPortfolio;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "fk_portfolio",
        referencedColumnName = "pk_portfolio",
        insertable = false,
        updatable = false,
        nullable = false
    )
    private Portfolio portfolio;

    @Column(name = "quantidade_critica", nullable = false)
    private Integer quantidadeCritica;

    @Column(name = "quantidade_maxima", nullable = false)
    private Integer quantidadeMaxima;

    @Column(name = "quantidade_exata", nullable = false)
    private Integer quantidadeExata = 0;
}

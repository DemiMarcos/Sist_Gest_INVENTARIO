package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "encomenda_item")
@Getter
@Setter
@NoArgsConstructor
public class EncomendaItem
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_item")
    private Integer pkItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_encomenda", nullable = false)
    private Encomenda encomenda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_portfolio", referencedColumnName = "pk_portfolio", nullable = false)
    private Portfolio portfolio;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;
}

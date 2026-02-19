package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "venda")
@Getter
@Setter
@NoArgsConstructor
public class Venda
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_venda")
    private Integer pkVenda;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    // fk_cliente -> pessoa(pk_pessoa)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "fk_cliente",
        referencedColumnName = "pk_pessoa",
        nullable = false
    )
    private Pessoa cliente;
}

package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "telefone",
    uniqueConstraints = @UniqueConstraint(name = "ux_telefone", columnNames =
    {
        "numero"
})
)
@Getter
@Setter
@NoArgsConstructor
public class Telefone
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_telefone")
    private Integer pkTelefone;

    @Column(name = "numero", nullable = false, length = 40)
    private String numero;
}

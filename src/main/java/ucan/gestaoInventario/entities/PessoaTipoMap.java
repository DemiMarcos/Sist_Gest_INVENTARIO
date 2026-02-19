package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "pessoa_tipo_map",
    uniqueConstraints = @UniqueConstraint(name = "ux_ptm", columnNames =
    {
        "fk_pessoa", "fk_tipo"
})
)
@Getter
@Setter
@NoArgsConstructor
public class PessoaTipoMap
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_map")
    private Integer pkMap;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_pessoa", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_tipo", nullable = false)
    private PessoaTipo tipo;
}

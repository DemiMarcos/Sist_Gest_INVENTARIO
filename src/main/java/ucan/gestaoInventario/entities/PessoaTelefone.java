package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "pessoa_telefone",
    uniqueConstraints = @UniqueConstraint(name = "ux_pt", columnNames =
    {
        "fk_pessoa", "fk_telefone"
})
)
@Getter
@Setter
@NoArgsConstructor
public class PessoaTelefone
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_pessoa_telefone")
    private Integer pkPessoaTelefone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_pessoa", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_telefone", nullable = false)
    private Telefone telefone;
}

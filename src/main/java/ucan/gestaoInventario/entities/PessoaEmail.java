package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "pessoa_email",
    uniqueConstraints = @UniqueConstraint(name = "ux_pe", columnNames =
    {
        "fk_pessoa", "fk_email"
})
)
@Getter
@Setter
@NoArgsConstructor
public class PessoaEmail
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_pessoa_email")
    private Integer pkPessoaEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_pessoa", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_email", nullable = false)
    private Email email;
}

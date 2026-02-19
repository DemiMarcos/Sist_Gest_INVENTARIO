package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "email",
    uniqueConstraints = @UniqueConstraint(name = "ux_email", columnNames =
    {
        "email"
})
)
@Getter
@Setter
@NoArgsConstructor
public class Email
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_email")
    private Integer pkEmail;

    @Column(name = "email", nullable = false, length = 120)
    private String email;
}

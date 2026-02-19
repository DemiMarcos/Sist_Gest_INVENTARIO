package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "localizacao",
    uniqueConstraints = @UniqueConstraint(columnNames =
    {
        "fk_armazem", "codigo"
})
)
public class Localizacao
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_localizacao")
    private Integer pkLocalizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_armazem", nullable = false)
    private Armazem armazem;

    @Column(nullable = false, length = 50)
    private String codigo;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Boolean activo = true;
}

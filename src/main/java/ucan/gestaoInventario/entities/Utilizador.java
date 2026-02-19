package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utilizador")
@Getter
@Setter
@NoArgsConstructor
public class Utilizador
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_utilizador")
    private Integer pkUtilizador;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "activo", nullable = false)
    private Boolean activo;
}

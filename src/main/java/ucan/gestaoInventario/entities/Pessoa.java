package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
public class Pessoa
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_pessoa")
    private Integer pkPessoa;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}

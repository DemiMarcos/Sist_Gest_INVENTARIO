package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pessoa_tipo")
@Getter
@Setter
@NoArgsConstructor
public class PessoaTipo
{

    @Id
    @Column(name = "pk_tipo", length = 20)
    private String pkTipo;
}

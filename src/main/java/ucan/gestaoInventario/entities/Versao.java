package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "versao")
@Getter
@Setter
@NoArgsConstructor
public class Versao
{

    @Id
    @Column(name = "pk_versao", length = 50)
    private String pkVersao;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;
}

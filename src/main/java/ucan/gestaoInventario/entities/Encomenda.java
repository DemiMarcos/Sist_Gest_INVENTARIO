package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "encomenda")
@Getter
@Setter
@NoArgsConstructor
public class Encomenda
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_encomenda")
    private Integer pkEncomenda;

    @Column(name = "data_encomenda", nullable = false)
    private LocalDateTime dataEncomenda;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_fornecedor", referencedColumnName = "pk_pessoa", nullable = false)
    private Pessoa fornecedor;
}

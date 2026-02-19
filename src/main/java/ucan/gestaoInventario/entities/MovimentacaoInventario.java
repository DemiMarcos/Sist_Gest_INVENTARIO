package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_inventario")
@Getter
@Setter
@NoArgsConstructor
public class MovimentacaoInventario
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_movimentacao")
    private Integer pkMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_portfolio", nullable = false)
    private Portfolio portfolio;

    /**
     * 'E' = Entrada | 'S' = Saída
     */
    @Column(name = "tipo_movimento", nullable = false, length = 1)
    private String tipoMovimento;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "data_movimento", nullable = false)
    private LocalDateTime dataMovimento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_armazem", nullable = false)
    private Armazem armazem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_localizacao")
    private Localizacao localizacao;

}

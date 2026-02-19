package ucan.gestaoInventario.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "produto_preco")
@Getter
@Setter
@NoArgsConstructor
public class ProdutoPreco
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mantém como estavas (não é o teu erro)
    @Column(name = "pk_preco")
    private Integer pkPreco;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_portfolio", referencedColumnName = "fk_portfolio", nullable = false)
    private Produto produto;

    @Column(name = "preco", nullable = false)
    private Double preco;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;
}

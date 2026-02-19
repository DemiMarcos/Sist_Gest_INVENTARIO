package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProdutoPrecoResponseRTO
{
    private Integer pkPreco;
    private String fkPortfolio;
    private Double preco;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
}

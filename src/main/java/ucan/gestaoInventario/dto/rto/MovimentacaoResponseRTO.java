package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MovimentacaoResponseRTO
{

    private Integer pkMovimentacao;
    private String fkPortfolio;
    private String tipoMovimento;
    private Integer quantidade;
    private LocalDateTime dataMovimento;
}

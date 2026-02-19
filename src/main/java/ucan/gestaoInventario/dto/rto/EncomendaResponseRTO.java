package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EncomendaResponseRTO
{
    private Integer pkEncomenda;
    private LocalDateTime dataEncomenda;
    private String estado;

    private Integer fkFornecedor;
    private String nomeFornecedor;
}

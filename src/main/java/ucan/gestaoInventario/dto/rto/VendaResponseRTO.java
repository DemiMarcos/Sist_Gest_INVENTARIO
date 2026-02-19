package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class VendaResponseRTO
{

    private Integer pkVenda;
    private LocalDateTime dataVenda;
    private String estado;

    private Integer fkCliente;
    private String nomeCliente;
}

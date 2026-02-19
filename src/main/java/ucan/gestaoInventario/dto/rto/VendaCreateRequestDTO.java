package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaCreateRequestDTO
{

    @NotNull(message = "fkCliente é obrigatório")
    private Integer fkCliente;
}

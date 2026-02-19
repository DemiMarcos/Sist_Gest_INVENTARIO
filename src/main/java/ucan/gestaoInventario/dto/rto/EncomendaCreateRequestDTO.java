package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncomendaCreateRequestDTO
{
    @NotNull(message = "fkFornecedor é obrigatório")
    private Integer fkFornecedor;
}

package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EncomendaEstadoUpdateDTO
{

    @NotBlank(message = "estado é obrigatório")
    private String estado;
}

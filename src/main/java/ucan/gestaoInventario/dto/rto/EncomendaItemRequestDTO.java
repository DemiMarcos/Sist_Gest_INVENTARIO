package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EncomendaItemRequestDTO
{

    @NotBlank(message = "fkPortfolio é obrigatório")
    private String fkPortfolio;

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 1, message = "quantidade deve ser >= 1")
    private Integer quantidade;
}

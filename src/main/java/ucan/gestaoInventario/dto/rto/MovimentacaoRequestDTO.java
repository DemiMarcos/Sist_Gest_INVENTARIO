package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MovimentacaoRequestDTO
{

    @NotBlank(message = "fk_portfolio é obrigatório")
    private String fkPortfolio;

    /**
     * 'E' = Entrada, 'S' = Saída
     */
    @NotNull(message = "tipo_movimento é obrigatório")
    @Pattern(regexp = "E|S", message = "tipo_movimento deve ser 'E' ou 'S'")
    private String tipoMovimento;

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 1, message = "quantidade deve ser >= 1")
    private Integer quantidade;

    @NotNull(message = "fk_armazem é obrigatório")
    private Integer fkArmazem;

    private Integer fkLocalizacao;
}

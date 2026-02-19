package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArmazemCreateRequestDTO
{

    @NotBlank(message = "nome é obrigatório")
    @Size(max = 100, message = "nome deve ter no máximo 100 caracteres")
    private String nome;
}

package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UtilizadorCreateRequestDTO
{

    @NotBlank(message = "username é obrigatório")
    @Size(max = 50, message = "username deve ter no máximo 50 caracteres")
    private String username;
}

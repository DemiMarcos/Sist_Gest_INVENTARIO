package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocalizacaoUpdateRequestDTO
{

    @Size(max = 50, message = "codigo deve ter no máximo 50 caracteres")
    private String codigo;

    @Size(max = 255, message = "descricao deve ter no máximo 255 caracteres")
    private String descricao;

    private Boolean activo;
}

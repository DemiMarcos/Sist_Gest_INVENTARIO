package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UtilizadorResponseRTO
{

    private Integer pkUtilizador;
    private String username;
    private Boolean activo;
}

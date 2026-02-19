package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArmazemResponseRTO
{

    private Integer pkArmazem;
    private String nome;
    private Boolean activo;
}

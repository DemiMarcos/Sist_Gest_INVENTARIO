package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalizacaoResponseRTO
{

    private Integer pkLocalizacao;
    private Integer fkArmazem;
    private String nomeArmazem;
    private String codigo;
    private String descricao;
    private Boolean activo;
}

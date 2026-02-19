package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EncomendaItemResponseRTO
{

    private Integer pkItem;
    private String fkPortfolio;
    private String descricao;
    private Integer quantidade;
}

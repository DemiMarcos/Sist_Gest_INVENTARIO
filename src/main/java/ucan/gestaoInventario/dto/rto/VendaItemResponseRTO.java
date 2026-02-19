package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VendaItemResponseRTO
{

    private Integer pkItem;
    private String fkPortfolio;
    private String descricao;
    private Integer quantidade;
    private Double precoUnitario;
}

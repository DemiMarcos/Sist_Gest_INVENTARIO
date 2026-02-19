package ucan.gestaoInventario.dto.rto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PortfolioNodeRTO
{

    private String codigo;
    private String descricao;
    private List<PortfolioNodeRTO> filhos = new ArrayList<>();

    public PortfolioNodeRTO(String codigo, String descricao)
    {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}

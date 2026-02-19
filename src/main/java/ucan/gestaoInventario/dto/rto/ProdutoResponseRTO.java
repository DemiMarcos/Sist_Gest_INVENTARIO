package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProdutoResponseRTO
{

    private String codigo;
    private String descricao;
    private Integer quantidadeCritica;
    private Integer quantidadeMaxima;
    private Integer stockAtual;
    private Double preco; // novo campo
}

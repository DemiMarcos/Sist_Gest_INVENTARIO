package ucan.gestaoInventario.dto.rto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponseRTO
{

    private Integer id;
    private String nome;
    private Boolean activo;

    private List<String> tipos;      // ["FORNECEDOR"], ["CLIENTE"] ou ambos
    private List<String> emails;
    private List<String> telefones;
}

package ucan.gestaoInventario.dto.rto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem
{

    private int linha;
    private MensagemTipo tipo;
    private String mensagem;
}

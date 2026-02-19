package ucan.gestaoInventario.dto.rto.relatorio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class MensagemLista
{

    private List<Mensagem> mensagemLista = new ArrayList<>();

    public void add(int linha, MensagemTipo tipo, String msg)
    {
        mensagemLista.add(new Mensagem(linha, tipo, msg));
    }

    // atalhos padronizados (fica bem para usar no import)
    public void erro(int linha, String msg)
    {
        add(linha, MensagemTipo.ERRO, msg);
    }

    public void alerta(int linha, String msg)
    {
        add(linha, MensagemTipo.ALERTA, msg);
    }

    public boolean temErros()
    {
        return mensagemLista.stream().anyMatch(m -> m.getTipo() == MensagemTipo.ERRO);
    }

    // ordena por linha e coloca ERRO antes de ALERTA
    public List<Mensagem> sort()
    {
        mensagemLista.sort(
            Comparator.comparingInt(Mensagem::getLinha)
                .thenComparingInt(m -> prioridade(m.getTipo()))
        );
        return mensagemLista;
    }

    private int prioridade(MensagemTipo tipo)
    {
        if (tipo == MensagemTipo.ERRO)
        {
            return 0;
        }
        return 1; // ALERTA
    }
}

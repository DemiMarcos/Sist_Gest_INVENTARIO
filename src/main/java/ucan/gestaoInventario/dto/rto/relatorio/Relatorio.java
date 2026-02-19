package ucan.gestaoInventario.dto.rto.relatorio;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Relatorio
{

    // data do excel
    private LocalDateTime dataExcel;

    // data atual na BD (versão atual)
    private LocalDateTime dataBD;

    // itens (quando persistido=true)
    private RepositorioItemLista repositorioItemLista;

    // mensagens (quando persistido=false ou quando quiseres avisos)
    private MensagemLista mensagemLista;

    // para o cliente saber se persistiu
    private boolean persistido;

    public Relatorio()
    {
        repositorioItemLista = new RepositorioItemLista();
        mensagemLista = new MensagemLista();
    }

    
    // helper (opcional, mas útil)
    public boolean temErros()
    {
        return mensagemLista.temErros();
    }

    public void setRepositorioItemLista(List<RepositorioItem> itens)
    {
        this.repositorioItemLista.setRepositorioItemLista(itens);
    }
}

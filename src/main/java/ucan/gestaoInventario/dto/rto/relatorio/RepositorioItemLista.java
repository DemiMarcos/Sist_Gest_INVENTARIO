package ucan.gestaoInventario.dto.rto.relatorio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RepositorioItemLista
{

    private List<RepositorioItem> repositorioItemLista;

    public RepositorioItemLista()
    {
        repositorioItemLista = new ArrayList<>();
    }

    
    public void add(RepositorioItem it)
    {
        repositorioItemLista.add(it);
    }

    // ordena pelo "codigo" usando a regra numérica por segmentos
    public List<RepositorioItem> sort()
    {
        repositorioItemLista.sort((a, b)
            -> RepositorioItem.compareCodigo(a.getCodigo(), b.getCodigo())
        );
        return repositorioItemLista;
    }
}

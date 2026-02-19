package ucan.gestaoInventario.dto.rto.relatorio;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "codigo"
)
public class RepositorioItem
{

    private int linha;
    private String codigo;
    private String designacao;

    // pai lógico (montado em memória)
    private RepositorioItem repositorioItemPai;

    // produto (se for folha)
    private Integer quantidadeCritica;
    private Integer quantidadeMaxima;

    public int compareCodigo(RepositorioItem ri)
    {
        return compareCodigo(this.codigo, ri.codigo);
    }

    // compara "1.2.10" com "1.2.3" numericamente por segmento
    public static int compareCodigo(String cd1, String cd2)
    {
        if (cd1 == null && cd2 == null)
        {
            return 0;
        }
        if (cd1 == null)
        {
            return -1;
        }
        if (cd2 == null)
        {
            return 1;
        }

        String[] a = cd1.split("\\.");
        String[] b = cd2.split("\\.");

        int min = Math.min(a.length, b.length);
        for (int i = 0; i < min; i++)
        {
            int x = parseIntSafe(a[i]);
            int y = parseIntSafe(b[i]);
            int rt = Integer.compare(x, y);
            if (rt != 0)
            {
                return rt;
            }
        }
        return Integer.compare(a.length, b.length);
    }

    private static int parseIntSafe(String s)
    {
        try
        {
            return Integer.parseInt(s.trim());
        }
        catch (Exception e)
        {
            return Integer.MIN_VALUE; // força "mal ordenado" aparecer cedo
        }
    }
}

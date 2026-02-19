package ucan.gestaoInventario.dto.rto;

public class RankingClienteValorRTO
{

    private Integer fkCliente;
    private String nomeCliente;
    private Double total;

    public RankingClienteValorRTO(Integer fkCliente, String nomeCliente, Double total)
    {
        this.fkCliente = fkCliente;
        this.nomeCliente = nomeCliente;
        this.total = total;
    }

    public Integer getFkCliente()
    {
        return fkCliente;
    }

    public String getNomeCliente()
    {
        return nomeCliente;
    }

    public Double getTotal()
    {
        return total;
    }
}

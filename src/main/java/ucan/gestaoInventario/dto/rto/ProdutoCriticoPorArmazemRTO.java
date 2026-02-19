package ucan.gestaoInventario.dto.rto;

public class ProdutoCriticoPorArmazemRTO
{

    private Integer fkArmazem;
    private String fkPortfolio;
    private String descricao;
    private Integer stockArmazem;
    private Integer quantidadeCritica;

    public ProdutoCriticoPorArmazemRTO(Integer fkArmazem, String fkPortfolio, String descricao, Integer stockArmazem, Integer quantidadeCritica)
    {
        this.fkArmazem = fkArmazem;
        this.fkPortfolio = fkPortfolio;
        this.descricao = descricao;
        this.stockArmazem = stockArmazem;
        this.quantidadeCritica = quantidadeCritica;
    }

    public Integer getFkArmazem()
    {
        return fkArmazem;
    }

    public String getFkPortfolio()
    {
        return fkPortfolio;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public Integer getStockArmazem()
    {
        return stockArmazem;
    }

    public Integer getQuantidadeCritica()
    {
        return quantidadeCritica;
    }
}

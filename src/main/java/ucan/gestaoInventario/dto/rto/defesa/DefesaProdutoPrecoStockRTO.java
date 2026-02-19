package ucan.gestaoInventario.dto.rto.defesa;

public class DefesaProdutoPrecoStockRTO
{

    private String fkPortfolio;
    private String descricao;
    private Double preco;
    private Integer stock;

    public DefesaProdutoPrecoStockRTO(String fkPortfolio, String descricao, Double preco, Integer stock)
    {
        this.fkPortfolio = fkPortfolio;
        this.descricao = descricao;
        this.preco = preco;
        this.stock = stock;
    }

    public String getFkPortfolio()
    {
        return fkPortfolio;
    }

    public void setFkPortfolio(String fkPortfolio)
    {
        this.fkPortfolio = fkPortfolio;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public void setDescricao(String descricao)
    {
        this.descricao = descricao;
    }

    public Double getPreco()
    {
        return preco;
    }

    public void setPreco(Double preco)
    {
        this.preco = preco;
    }

    public Integer getStock()
    {
        return stock;
    }

    public void setStock(Integer stock)
    {
        this.stock = stock;
    }
}

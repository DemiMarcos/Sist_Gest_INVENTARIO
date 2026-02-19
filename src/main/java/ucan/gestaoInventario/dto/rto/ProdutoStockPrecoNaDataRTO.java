package ucan.gestaoInventario.dto.rto;

import java.time.LocalDateTime;

public class ProdutoStockPrecoNaDataRTO
{

    private String fkPortfolio;
    private String descricao;
    private Double preco;
    private Integer stock;
    private LocalDateTime dataRef;

    public ProdutoStockPrecoNaDataRTO(String fkPortfolio, String descricao, Double preco, Integer stock, LocalDateTime dataRef)
    {
        this.fkPortfolio = fkPortfolio;
        this.descricao = descricao;
        this.preco = preco;
        this.stock = stock;
        this.dataRef = dataRef;
    }

    public String getFkPortfolio()
    {
        return fkPortfolio;
    }

    public String getDescricao()
    {
        return descricao;
    }

    public Double getPreco()
    {
        return preco;
    }

    public Integer getStock()
    {
        return stock;
    }

    public LocalDateTime getDataRef()
    {
        return dataRef;
    }
}

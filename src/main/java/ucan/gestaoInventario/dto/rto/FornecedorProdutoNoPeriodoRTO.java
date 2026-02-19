package ucan.gestaoInventario.dto.rto;

public class FornecedorProdutoNoPeriodoRTO
{

    private Integer fkFornecedor;
    private String nomeFornecedor;
    private String fkPortfolio;
    private String descricaoProduto;
    private Long totalQuantidade;

    public FornecedorProdutoNoPeriodoRTO(
        Integer fkFornecedor,
        String nomeFornecedor,
        String fkPortfolio,
        String descricaoProduto,
        Long totalQuantidade
    )
    {
        this.fkFornecedor = fkFornecedor;
        this.nomeFornecedor = nomeFornecedor;
        this.fkPortfolio = fkPortfolio;
        this.descricaoProduto = descricaoProduto;
        this.totalQuantidade = totalQuantidade;
    }

    public Integer getFkFornecedor()
    {
        return fkFornecedor;
    }

    public String getNomeFornecedor()
    {
        return nomeFornecedor;
    }

    public String getFkPortfolio()
    {
        return fkPortfolio;
    }

    public String getDescricaoProduto()
    {
        return descricaoProduto;
    }

    public Long getTotalQuantidade()
    {
        return totalQuantidade;
    }
}

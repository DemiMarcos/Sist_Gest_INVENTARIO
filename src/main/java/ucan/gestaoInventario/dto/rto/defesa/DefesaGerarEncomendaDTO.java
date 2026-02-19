package ucan.gestaoInventario.dto.rto.defesa;

import java.time.LocalDateTime;

public class DefesaGerarEncomendaDTO
{

    private Integer fkArmazem;
    private LocalDateTime dataRef;
    private Double precoMin;
    private String prefixo;      // opcional (ex: "2.1.")
    private Integer fkFornecedor;

    public Integer getFkArmazem()
    {
        return fkArmazem;
    }

    public void setFkArmazem(Integer fkArmazem)
    {
        this.fkArmazem = fkArmazem;
    }

    public LocalDateTime getDataRef()
    {
        return dataRef;
    }

    public void setDataRef(LocalDateTime dataRef)
    {
        this.dataRef = dataRef;
    }

    public Double getPrecoMin()
    {
        return precoMin;
    }

    public void setPrecoMin(Double precoMin)
    {
        this.precoMin = precoMin;
    }

    public String getPrefixo()
    {
        return prefixo;
    }

    public void setPrefixo(String prefixo)
    {
        this.prefixo = prefixo;
    }

    public Integer getFkFornecedor()
    {
        return fkFornecedor;
    }

    public void setFkFornecedor(Integer fkFornecedor)
    {
        this.fkFornecedor = fkFornecedor;
    }
}

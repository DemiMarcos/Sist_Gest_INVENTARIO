package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ProdutoStockPrecoNaDataRequestDTO
{

    @NotEmpty
    private List<String> fkPortfolios;

    @NotNull
    private LocalDateTime dataRef;

    @NotNull
    private Double precoMin;

    // opcional: se null => stock global; se preenchido => stock por armazém
    private Integer fkArmazem;

    public List<String> getFkPortfolios()
    {
        return fkPortfolios;
    }

    public void setFkPortfolios(List<String> fkPortfolios)
    {
        this.fkPortfolios = fkPortfolios;
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

    public Integer getFkArmazem()
    {
        return fkArmazem;
    }

    public void setFkArmazem(Integer fkArmazem)
    {
        this.fkArmazem = fkArmazem;
    }
}

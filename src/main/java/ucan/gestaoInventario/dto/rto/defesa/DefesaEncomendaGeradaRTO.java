package ucan.gestaoInventario.dto.rto.defesa;

import java.time.LocalDateTime;

public class DefesaEncomendaGeradaRTO
{

    private Integer pkEncomenda;
    private Integer totalItens;
    private LocalDateTime dataEncomenda;

    public DefesaEncomendaGeradaRTO(
        Integer pkEncomenda,
        Integer totalItens,
        LocalDateTime dataEncomenda
    )
    {
        this.pkEncomenda = pkEncomenda;
        this.totalItens = totalItens;
        this.dataEncomenda = dataEncomenda;
    }

    public Integer getPkEncomenda()
    {
        return pkEncomenda;
    }

    public void setPkEncomenda(Integer pkEncomenda)
    {
        this.pkEncomenda = pkEncomenda;
    }

    public Integer getTotalItens()
    {
        return totalItens;
    }

    public void setTotalItens(Integer totalItens)
    {
        this.totalItens = totalItens;
    }

    public LocalDateTime getDataEncomenda()
    {
        return dataEncomenda;
    }

    public void setDataEncomenda(LocalDateTime dataEncomenda)
    {
        this.dataEncomenda = dataEncomenda;
    }
}

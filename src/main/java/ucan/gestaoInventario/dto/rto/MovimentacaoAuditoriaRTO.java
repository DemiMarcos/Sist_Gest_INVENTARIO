package ucan.gestaoInventario.dto.rto;

import java.time.LocalDateTime;

public class MovimentacaoAuditoriaRTO
{

    private Integer pkMovimentacao;
    private String fkPortfolio;
    private String tipoMovimento;
    private Integer quantidade;
    private LocalDateTime dataMovimento;
    private Integer fkArmazem;
    private Integer fkLocalizacao;

    public MovimentacaoAuditoriaRTO(
        Integer pkMovimentacao,
        String fkPortfolio,
        String tipoMovimento,
        Integer quantidade,
        LocalDateTime dataMovimento,
        Integer fkArmazem,
        Integer fkLocalizacao
    )
    {
        this.pkMovimentacao = pkMovimentacao;
        this.fkPortfolio = fkPortfolio;
        this.tipoMovimento = tipoMovimento;
        this.quantidade = quantidade;
        this.dataMovimento = dataMovimento;
        this.fkArmazem = fkArmazem;
        this.fkLocalizacao = fkLocalizacao;
    }

    public Integer getPkMovimentacao()
    {
        return pkMovimentacao;
    }

    public String getFkPortfolio()
    {
        return fkPortfolio;
    }

    public String getTipoMovimento()
    {
        return tipoMovimento;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }

    public LocalDateTime getDataMovimento()
    {
        return dataMovimento;
    }

    public Integer getFkArmazem()
    {
        return fkArmazem;
    }

    public Integer getFkLocalizacao()
    {
        return fkLocalizacao;
    }
}

package ucan.gestaoInventario.dto.rto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class RankingClientesRequestDTO
{

    @NotNull
    private LocalDateTime ini;

    @NotNull
    private LocalDateTime fim;

    // opcional: se vier vazio/null => ranking geral
    private List<String> fkPortfolios;

    public LocalDateTime getIni()
    {
        return ini;
    }

    public void setIni(LocalDateTime ini)
    {
        this.ini = ini;
    }

    public LocalDateTime getFim()
    {
        return fim;
    }

    public void setFim(LocalDateTime fim)
    {
        this.fim = fim;
    }

    public List<String> getFkPortfolios()
    {
        return fkPortfolios;
    }

    public void setFkPortfolios(List<String> fkPortfolios)
    {
        this.fkPortfolios = fkPortfolios;
    }
}

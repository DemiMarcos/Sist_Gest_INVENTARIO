package ucan.gestaoInventario.dto.rto.avancadas;

public interface RupturaIminenteRTO
{

    String getFkPortfolio();

    String getDescricao();

    Integer getStockAtual();

    Integer getSaidaPeriodo();

    Double getMediaSaidaDia();

    Double getConsumoPrevisto();
}

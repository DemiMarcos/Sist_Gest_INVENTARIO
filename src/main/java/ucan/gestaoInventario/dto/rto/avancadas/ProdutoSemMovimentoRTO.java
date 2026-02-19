package ucan.gestaoInventario.dto.rto.avancadas;

import java.time.LocalDateTime;

public interface ProdutoSemMovimentoRTO {
    String getFkPortfolio();
    String getDescricao();
    LocalDateTime getUltimaMovimentacao();
}

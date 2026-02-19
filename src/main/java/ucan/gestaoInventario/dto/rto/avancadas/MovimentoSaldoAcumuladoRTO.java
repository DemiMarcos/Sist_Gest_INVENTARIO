package ucan.gestaoInventario.dto.rto.avancadas;

import java.time.LocalDateTime;

public interface MovimentoSaldoAcumuladoRTO
{

    Integer getIdMov();

    LocalDateTime getDataMov();

    String getTipo();

    Integer getQuantidade();

    Integer getSaldoAcumulado();
}

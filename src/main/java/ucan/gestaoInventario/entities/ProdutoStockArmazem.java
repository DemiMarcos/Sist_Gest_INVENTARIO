package ucan.gestaoInventario.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Chave composta de ProdutoStock (saldo por armazém).
 *
 * Nome escolhido para ficar sugestivo (Produto + Stock + Armazém).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProdutoStockArmazem implements Serializable
{
    private String fkPortfolio;
    private Integer fkArmazem;
}

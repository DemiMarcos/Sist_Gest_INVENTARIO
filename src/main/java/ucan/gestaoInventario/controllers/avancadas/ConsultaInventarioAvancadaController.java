package ucan.gestaoInventario.controllers.avancadas;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.avancadas.MovimentoSaldoAcumuladoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoABCRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoCompradoJuntoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.ProdutoSemMovimentoRTO;
import ucan.gestaoInventario.dto.rto.avancadas.RupturaIminenteRTO;
import ucan.gestaoInventario.services.avancadas.ConsultaInventarioAvancadaService;

@RestController
@RequestMapping("/inventario/consultas/avancadas")
public class ConsultaInventarioAvancadaController
{

    private final ConsultaInventarioAvancadaService service;

    public ConsultaInventarioAvancadaController(ConsultaInventarioAvancadaService service)
    {
        this.service = service;
    }

    // 1) Auditoria com saldo acumulado (Window)
    @GetMapping("/movimentos/saldo-acumulado")
    public List<MovimentoSaldoAcumuladoRTO> saldoAcumulado(
        @RequestParam String fkPortfolio,
        @RequestParam Integer fkArmazem,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtFim
    )
    {
        return service.auditoriaSaldoAcumulado(fkPortfolio, fkArmazem, dtInicio, dtFim);
    }

    // 2) Curva ABC (CTE + Window)
    @GetMapping("/vendas/curva-abc")
    public List<ProdutoABCRTO> curvaABC(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtFim
    )
    {
        return service.curvaABC(dtInicio, dtFim);
    }

    // 3) Produtos sem movimento há X dias
    @GetMapping("/produtos/sem-movimento")
    public List<ProdutoSemMovimentoRTO> produtosSemMovimento(
        @RequestParam Integer dias,
        @RequestParam(required = false) String prefixo
    )
    {
        return service.produtosSemMovimento(dias, prefixo);
    }

    // 4) Ruptura iminente (previsão por consumo médio)
    @GetMapping("/produtos/ruptura-iminente")
    public List<RupturaIminenteRTO> rupturaIminente(
        @RequestParam Integer fkArmazem,
        @RequestParam Integer diasHistorico,
        @RequestParam Integer diasPrevistos
    )
    {
        return service.rupturaIminente(fkArmazem, diasHistorico, diasPrevistos);
    }

    // 5) Produtos comprados juntos (associação por venda)
    @GetMapping("/vendas/produtos-juntos")
    public List<ProdutoCompradoJuntoRTO> produtosJuntos(
        @RequestParam String fkPortfolioBase,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtInicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dtFim,
        @RequestParam(defaultValue = "10") Integer top
    )
    {
        return service.produtosCompradosJuntos(fkPortfolioBase, dtInicio, dtFim, top);
    }
}

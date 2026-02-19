package ucan.gestaoInventario.controllers.defesa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.gestaoInventario.dto.rto.defesa.DefesaEncomendaGeradaRTO;
import ucan.gestaoInventario.dto.rto.defesa.DefesaGerarEncomendaDTO;
import ucan.gestaoInventario.dto.rto.defesa.DefesaProdutoPrecoStockRTO;
import ucan.gestaoInventario.services.defesa.DefesaService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/defesa")
public class DefesaController
{

    private final DefesaService defesaService;

    public DefesaController(DefesaService defesaService)
    {
        this.defesaService = defesaService;
    }

    // 1) listar preços + stock numa data + preço >= X
    // Ex:
    // GET /api/defesa/produtos/precos-stock?fkArmazem=1&dataRef=2026-02-13T10:00:00&precoMin=100&prefixo=2.1.
    @GetMapping("/produtos/precos-stock")
    public ResponseEntity<List<DefesaProdutoPrecoStockRTO>> listarPrecoStock(
        @RequestParam("fkArmazem") Integer fkArmazem,
        @RequestParam("dataRef") LocalDateTime dataRef,
        @RequestParam("precoMin") Double precoMin,
        @RequestParam(value = "prefixo", required = false) String prefixo
    )
    {
        return ResponseEntity.ok(
            defesaService.listarPrecoStockNaData(fkArmazem, dataRef, precoMin, prefixo)
        );
    }

    // 2) gerar encomenda automática com base nos filtros
    @PostMapping("/encomendas/gerar")
    public ResponseEntity<DefesaEncomendaGeradaRTO> gerarEncomenda(
        @RequestBody DefesaGerarEncomendaDTO dto
    )
    {
        return ResponseEntity.ok(defesaService.gerarEncomenda(dto));
    }
}

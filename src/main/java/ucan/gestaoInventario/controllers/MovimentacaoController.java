package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import ucan.gestaoInventario.dto.rto.MovimentacaoRequestDTO;
import ucan.gestaoInventario.dto.rto.MovimentacaoResponseRTO;
import ucan.gestaoInventario.services.MovimentacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
public class MovimentacaoController
{

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service)
    {
        this.service = service;
    }

    // POST /inventario/movimentos
    @PostMapping("/movimentos")
    public ResponseEntity<MovimentacaoResponseRTO> registar(
        @Valid @RequestBody MovimentacaoRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.registarMovimento(dto));
    }

    // GET /inventario/movimentos
    // GET /inventario/movimentos?fkPortfolio=1.1.1.1
    @GetMapping("/movimentos")
    public ResponseEntity<List<MovimentacaoResponseRTO>> listar(
        @RequestParam(value = "fkPortfolio", required = false) String fkPortfolio
    )
    {
        return ResponseEntity.ok(service.listarMovimentos(fkPortfolio));
    }
}

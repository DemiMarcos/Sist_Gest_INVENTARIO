package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.gestaoInventario.services.UtilizadorService;
import java.util.List;
import ucan.gestaoInventario.dto.rto.UtilizadorCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.UtilizadorResponseRTO;

@RestController
@RequestMapping("/inventario/utilizadores")
public class UtilizadorController
{

    private final UtilizadorService service;

    public UtilizadorController(UtilizadorService service)
    {
        this.service = service;
    }

    // POST /inventario/utilizadores
    @PostMapping
    public ResponseEntity<UtilizadorResponseRTO> criar(
        @Valid @RequestBody UtilizadorCreateRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.criar(dto));
    }

    // GET /inventario/utilizadores
    @GetMapping
    public ResponseEntity<List<UtilizadorResponseRTO>> listar()
    {
        return ResponseEntity.ok(service.listar());
    }

    // PATCH /inventario/utilizadores/{id}/activar
    @PatchMapping("/{id}/activar")
    public ResponseEntity<UtilizadorResponseRTO> activar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.activar(id));
    }

    // PATCH /inventario/utilizadores/{id}/desactivar
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UtilizadorResponseRTO> desactivar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.desactivar(id));
    }
}

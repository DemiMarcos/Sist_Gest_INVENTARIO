package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.gestaoInventario.dto.rto.ArmazemCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.ArmazemResponseRTO;
import ucan.gestaoInventario.dto.rto.ArmazemUpdateRequestDTO;
import ucan.gestaoInventario.services.ArmazemService;

import java.util.List;

@RestController
@RequestMapping("/inventario/armazens")
public class ArmazemController
{

    private final ArmazemService service;

    public ArmazemController(ArmazemService service)
    {
        this.service = service;
    }

    // POST /inventario/armazens
    @PostMapping
    public ResponseEntity<ArmazemResponseRTO> criar(@Valid @RequestBody ArmazemCreateRequestDTO dto)
    {
        return ResponseEntity.ok(service.criar(dto));
    }

    // GET /inventario/armazens
    @GetMapping
    public ResponseEntity<List<ArmazemResponseRTO>> listar()
    {
        return ResponseEntity.ok(service.listar());
    }

    // GET /inventario/armazens/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ArmazemResponseRTO> detalhar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.detalhar(id));
    }

    // PUT /inventario/armazens/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ArmazemResponseRTO> atualizar(
        @PathVariable Integer id,
        @Valid @RequestBody ArmazemUpdateRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // PATCH /inventario/armazens/{id}/ativar
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ArmazemResponseRTO> ativar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.ativar(id));
    }

    // PATCH /inventario/armazens/{id}/desativar
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ArmazemResponseRTO> desativar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.desativar(id));
    }
}

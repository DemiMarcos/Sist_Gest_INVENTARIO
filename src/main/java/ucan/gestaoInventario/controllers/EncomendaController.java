package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.EncomendaCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.EncomendaResponseRTO;
import ucan.gestaoInventario.dto.rto.EncomendaReceberDTO;
import ucan.gestaoInventario.services.EncomendaService;

import java.util.List;

@RestController
@RequestMapping("/inventario/encomendas")
public class EncomendaController
{

    private final EncomendaService service;

    public EncomendaController(EncomendaService service)
    {
        this.service = service;
    }

    // POST /inventario/encomendas
    @PostMapping
    public ResponseEntity<EncomendaResponseRTO> criar(@Valid @RequestBody EncomendaCreateRequestDTO dto)
    {
        return ResponseEntity.ok(service.criar(dto));
    }

    // GET /inventario/encomendas
    @GetMapping
    public ResponseEntity<List<EncomendaResponseRTO>> listar()
    {
        return ResponseEntity.ok(service.listar());
    }

    // GET /inventario/encomendas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EncomendaResponseRTO> detalhar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.detalhar(id));
    }

    /**
     * PATCH /inventario/encomendas/{id}/receber Body exemplo: { "fkArmazem": 1,
     * "fkLocalizacao": 2 } fkLocalizacao é opcional.
     */
    @PatchMapping("/{id}/receber")
    public ResponseEntity<EncomendaResponseRTO> receber(
        @PathVariable Integer id,
        @Valid @RequestBody EncomendaReceberDTO dto
    )
    {
        return ResponseEntity.ok(service.receber(id, dto));
    }
}

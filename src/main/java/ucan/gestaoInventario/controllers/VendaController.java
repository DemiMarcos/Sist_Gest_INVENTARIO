package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.*;
import ucan.gestaoInventario.services.VendaService;

import java.util.List;

@RestController
@RequestMapping("/inventario/vendas")
public class VendaController
{

    private final VendaService service;

    public VendaController(VendaService service)
    {
        this.service = service;
    }

    // POST /inventario/vendas
    @PostMapping
    public ResponseEntity<VendaResponseRTO> criar(@Valid @RequestBody VendaCreateRequestDTO dto)
    {
        return ResponseEntity.ok(service.criar(dto));
    }

    // GET /inventario/vendas
    @GetMapping
    public ResponseEntity<List<VendaResponseRTO>> listar()
    {
        return ResponseEntity.ok(service.listar());
    }

    // GET /inventario/vendas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseRTO> detalhar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.detalhar(id));
    }

    // POST /inventario/vendas/{id}/itens
    @PostMapping("/{id}/itens")
    public ResponseEntity<VendaItemResponseRTO> adicionarItem(
        @PathVariable Integer id,
        @Valid @RequestBody VendaItemCreateDTO dto
    )
    {
        return ResponseEntity.ok(service.adicionarItem(id, dto));
    }

    // GET /inventario/vendas/{id}/itens
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<VendaItemResponseRTO>> listarItens(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.listarItens(id));
    }

    // DELETE /inventario/vendas/{id}/itens/{pkItem}
    @DeleteMapping("/{id}/itens/{pkItem}")
    public ResponseEntity<?> removerItem(@PathVariable Integer id, @PathVariable Integer pkItem)
    {
        service.removerItem(id, pkItem);
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /inventario/vendas/{id}/confirmar?fkArmazem=1&fkLocalizacao=2
     * fkLocalizacao é opcional.
     */
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<VendaResponseRTO> confirmar(
        @PathVariable Integer id,
        @RequestParam("fkArmazem") Integer fkArmazem,
        @RequestParam(value = "fkLocalizacao", required = false) Integer fkLocalizacao
    )
    {
        return ResponseEntity.ok(service.confirmar(id, fkArmazem, fkLocalizacao));
    }
}

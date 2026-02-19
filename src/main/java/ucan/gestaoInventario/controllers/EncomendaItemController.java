package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.EncomendaItemCreateDTO;
import ucan.gestaoInventario.dto.rto.EncomendaItemResponseRTO;
import ucan.gestaoInventario.services.EncomendaItemService;

import java.util.List;

@RestController
@RequestMapping("/inventario/encomendas")
public class EncomendaItemController
{

    private final EncomendaItemService service;

    public EncomendaItemController(EncomendaItemService service)
    {
        this.service = service;
    }

    // POST /inventario/encomendas/{id}/itens
    @PostMapping("/{id}/itens")
    public ResponseEntity<EncomendaItemResponseRTO> adicionar(
        @PathVariable Integer id,
        @Valid @RequestBody EncomendaItemCreateDTO dto
    )
    {
        return ResponseEntity.ok(service.adicionarItem(id, dto));
    }

    // GET /inventario/encomendas/{id}/itens
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<EncomendaItemResponseRTO>> listar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.listarItens(id));
    }

    // DELETE /inventario/encomendas/{id}/itens/{pkItem}
    @DeleteMapping("/{id}/itens/{pkItem}")
    public ResponseEntity<?> remover(@PathVariable Integer id, @PathVariable Integer pkItem)
    {
        service.removerItem(id, pkItem);
        return ResponseEntity.ok().build();
    }
}

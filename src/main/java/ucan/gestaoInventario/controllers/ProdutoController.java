package ucan.gestaoInventario.controllers;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.ProdutoPrecoRequestDTO;
import ucan.gestaoInventario.dto.rto.ProdutoPrecoResponseRTO;
import ucan.gestaoInventario.dto.rto.ProdutoResponseRTO;
import ucan.gestaoInventario.services.ProdutoService;

import java.util.List;

@RestController
@RequestMapping("/inventario/produtos")
public class ProdutoController
{
    private final ProdutoService service;

    public ProdutoController(ProdutoService service)
    {
        this.service = service;
    }

    // trim automático em Strings
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    // GET /inventario/produtos
    // GET /inventario/produtos?nome=...
    // GET /inventario/produtos?prefixo=1.1
    // GET /inventario/produtos?estado=ABAIXO_CRITICO
    @GetMapping
    public ResponseEntity<List<ProdutoResponseRTO>> listar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String prefixo,
        @RequestParam(required = false) String estado
    )
    {
        return ResponseEntity.ok(service.listar(nome, prefixo, estado));
    }

    // GET /inventario/produtos/{codigo}
    @GetMapping("/{codigo}")
    public ResponseEntity<ProdutoResponseRTO> detalhar(@PathVariable String codigo)
    {
        return ResponseEntity.ok(service.detalhar(codigo));
    }

    // PATCH /inventario/produtos/{codigo}/preco
    @PatchMapping("/{codigo}/preco")
    public ResponseEntity<ProdutoResponseRTO> alterarPreco(
        @PathVariable String codigo,
        @RequestBody ProdutoPrecoRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.alterarPreco(codigo, dto));
    }

    // GET /inventario/produtos/{codigo}/precos  (HISTÓRICO)
    @GetMapping("/{codigo}/precos")
    public ResponseEntity<List<ProdutoPrecoResponseRTO>> historicoPrecos(@PathVariable String codigo)
    {
        return ResponseEntity.ok(service.listarHistoricoPrecos(codigo));
    }
}

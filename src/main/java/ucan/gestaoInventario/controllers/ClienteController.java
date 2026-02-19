package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.PessoaCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.PessoaResponseRTO;
import ucan.gestaoInventario.dto.rto.PessoaUpdateRequestDTO;
import ucan.gestaoInventario.services.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/inventario/clientes")
public class ClienteController
{

    private final ClienteService service;

    public ClienteController(ClienteService service)
    {
        this.service = service;
    }

    // POST /inventario/clientes
    @PostMapping
    public ResponseEntity<PessoaResponseRTO> criar(@Valid @RequestBody PessoaCreateRequestDTO dto)
    {
        return ResponseEntity.ok(service.criar(dto));
    }

    // GET /inventario/clientes
    @GetMapping
    public ResponseEntity<List<PessoaResponseRTO>> listar()
    {
        return ResponseEntity.ok(service.listar());
    }

    // GET /inventario/clientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseRTO> detalhar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.detalhar(id));
    }

    // PUT /inventario/clientes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseRTO> atualizar(
        @PathVariable Integer id,
        @Valid @RequestBody PessoaUpdateRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // DELETE /inventario/clientes/{id}  (delete lógico)
    @DeleteMapping("/{id}")
    public ResponseEntity<PessoaResponseRTO> desativar(@PathVariable Integer id)
    {
        return ResponseEntity.ok(service.desativar(id));
    }

}

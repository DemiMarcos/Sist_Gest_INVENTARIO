package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.LocalizacaoCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.LocalizacaoResponseRTO;
import ucan.gestaoInventario.dto.rto.LocalizacaoUpdateRequestDTO;
import ucan.gestaoInventario.services.LocalizacaoService;

import java.util.List;

@RestController
@RequestMapping("/inventario/armazens")
@RequiredArgsConstructor
public class LocalizacaoController
{

    private final LocalizacaoService service;

    @PostMapping("/{fkArmazem}/localizacoes")
    public ResponseEntity<LocalizacaoResponseRTO> criar(
        @PathVariable Integer fkArmazem,
        @Valid @RequestBody LocalizacaoCreateRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.criar(fkArmazem, dto));
    }

    @GetMapping("/{fkArmazem}/localizacoes")
    public ResponseEntity<List<LocalizacaoResponseRTO>> listar(
        @PathVariable Integer fkArmazem,
        @RequestParam(value = "soActivas", required = false) Boolean soActivas
    )
    {
        return ResponseEntity.ok(service.listarPorArmazem(fkArmazem, soActivas));
    }

    @GetMapping("/{fkArmazem}/localizacoes/{pkLocalizacao}")
    public ResponseEntity<LocalizacaoResponseRTO> detalhar(
        @PathVariable Integer fkArmazem,
        @PathVariable Integer pkLocalizacao
    )
    {
        return ResponseEntity.ok(service.detalhar(fkArmazem, pkLocalizacao));
    }

    @PatchMapping("/{fkArmazem}/localizacoes/{pkLocalizacao}")
    public ResponseEntity<LocalizacaoResponseRTO> atualizar(
        @PathVariable Integer fkArmazem,
        @PathVariable Integer pkLocalizacao,
        @RequestBody LocalizacaoUpdateRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.atualizar(fkArmazem, pkLocalizacao, dto));
    }

    @DeleteMapping("/{fkArmazem}/localizacoes/{pkLocalizacao}")
    public ResponseEntity<?> apagar(
        @PathVariable Integer fkArmazem,
        @PathVariable Integer pkLocalizacao
    )
    {
        service.apagar(fkArmazem, pkLocalizacao);
        return ResponseEntity.ok().build();
    }
}

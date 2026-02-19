package ucan.gestaoInventario.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import ucan.gestaoInventario.dto.rto.ProdutoStockPrecoNaDataRTO;
import ucan.gestaoInventario.services.RelatorioService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inventario/relatorios")
@RequiredArgsConstructor
public class RelatorioController
{

    private final RelatorioService relatorioService;

    @GetMapping("/produtos")
    public List<ProdutoStockPrecoNaDataRTO> listarProdutosStockPrecoPorGrupoNaData(
        @RequestParam String prefixo,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataRef,
        @RequestParam(defaultValue = "0") Double precoMin
    )
    {
        return relatorioService.listarProdutosStockPrecoPorGrupoNaData(prefixo, dataRef, precoMin);
    }
}

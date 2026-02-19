package ucan.gestaoInventario.controllers;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucan.gestaoInventario.dto.rto.*;
import ucan.gestaoInventario.services.ConsultaInventarioService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inventario/consultas")
public class ConsultaInventarioController
{

    private final ConsultaInventarioService service;

    public ConsultaInventarioController(ConsultaInventarioService service)
    {
        this.service = service;
    }

    // =========================
    // CONSULTA 1 (Enunciado #13)
    // POST /inventario/consultas/produtos/stock-preco-na-data
    // =========================
    @PostMapping("/produtos/stock-preco-na-data")
    public ResponseEntity<List<ProdutoStockPrecoNaDataRTO>> stockPrecoNaData(
        @Valid @RequestBody ProdutoStockPrecoNaDataRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.listarStockEPrecoNaData(dto));
    }

    // =========================
    // CONSULTA 2
    // POST /inventario/consultas/vendas/ranking-clientes
    // =========================
    @PostMapping("/vendas/ranking-clientes")
    public ResponseEntity<List<RankingClienteValorRTO>> rankingClientes(
        @Valid @RequestBody RankingClientesRequestDTO dto
    )
    {
        return ResponseEntity.ok(service.rankingClientesPorValor(dto));
    }

    // =========================
    // CONSULTA 3
    // GET /inventario/consultas/produtos/criticos-por-armazem?fkArmazem=2&prefixo=2.1.
    // =========================
    @GetMapping("/produtos/criticos-por-armazem")
    public ResponseEntity<List<ProdutoCriticoPorArmazemRTO>> criticosPorArmazem(
        @RequestParam("fkArmazem") Integer fkArmazem,
        @RequestParam(value = "prefixo", required = false) String prefixo
    )
    {
        return ResponseEntity.ok(service.listarCriticosPorArmazem(fkArmazem, prefixo));
    }

    // =========================
    // CONSULTA 4
    // GET /inventario/consultas/movimentos/auditoria?ini=2026-02-01T00:00:00&fim=2026-02-09T23:59:59&fkArmazem=2&fkLocalizacao=7&fkPortfolio=2.1.1.3
    // =========================
    @GetMapping("/movimentos/auditoria")
    public ResponseEntity<List<MovimentacaoAuditoriaRTO>> auditoriaMovimentos(
        @RequestParam("ini") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ini,
        @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
        @RequestParam("fkArmazem") Integer fkArmazem,
        @RequestParam(value = "fkLocalizacao", required = false) Integer fkLocalizacao,
        @RequestParam(value = "fkPortfolio", required = false) String fkPortfolio
    )
    {
        return ResponseEntity.ok(service.auditoriaMovimentos(ini, fim, fkArmazem, fkLocalizacao, fkPortfolio));
    }

    // =========================
    // CONSULTA 5
    // GET /inventario/consultas/fornecedores/produtos-no-periodo?sufixoNome=a&ini=2026-02-01T00:00:00&fim=2026-02-09T23:59:59&prefixo=2.1.
    // =========================
    @GetMapping("/fornecedores/produtos-no-periodo")
    public ResponseEntity<List<FornecedorProdutoNoPeriodoRTO>> fornecedoresProdutosNoPeriodo(
        @RequestParam("sufixoNome") String sufixoNome,
        @RequestParam("ini") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ini,
        @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
        @RequestParam(value = "prefixo", required = false) String prefixo
    )
    {
        return ResponseEntity.ok(service.fornecedoresProdutosNoPeriodo(sufixoNome, ini, fim, prefixo));
    }
}

package ucan.gestaoInventario.controllers;

import ucan.gestaoInventario.dto.rto.relatorio.Relatorio;
import ucan.gestaoInventario.services.PortfolioImportService;
import ucan.gestaoInventario.services.PortfolioTreeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/inventario/portfolio")
public class PortfolioController
{

    private final PortfolioImportService portfolioImportService;
    private final PortfolioTreeService portfolioTreeService;

    public PortfolioController(
        PortfolioImportService portfolioImportService,
        PortfolioTreeService portfolioTreeService
    )
    {
        this.portfolioImportService = portfolioImportService;
        this.portfolioTreeService = portfolioTreeService;
    }

    @PostMapping(value = "/importar", consumes = "multipart/form-data")
    public ResponseEntity<Relatorio> importarPortfolio(@RequestParam("file") MultipartFile file) throws Exception
    {

        Relatorio rel = portfolioImportService.importar(file);

        // Se houver erros -> devolve 422 (mas com o relatório no body, como o professor quer)
        if (rel.getMensagemLista() != null && rel.getMensagemLista().temErros())
        {
            rel.getMensagemLista().sort();
            return ResponseEntity.unprocessableEntity().body(rel);
        }

        return ResponseEntity.ok(rel);
    }

    @GetMapping("/arvore")
    public ResponseEntity<?> obterArvore()
    {
        return ResponseEntity.ok(portfolioTreeService.obterArvore());
    }
}

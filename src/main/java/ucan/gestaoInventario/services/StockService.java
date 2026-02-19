package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucan.gestaoInventario.entities.Produto;
import ucan.gestaoInventario.entities.ProdutoStock;
import ucan.gestaoInventario.entities.ProdutoStockArmazem;
import ucan.gestaoInventario.repositories.ProdutoRepository;
import ucan.gestaoInventario.repositories.ProdutoStockRepository;

@Service
public class StockService
{

    private final ProdutoRepository produtoRepo;
    private final ProdutoStockRepository produtoStockRepo;

    public StockService(
        ProdutoRepository produtoRepo,
        ProdutoStockRepository produtoStockRepo
    )
    {
        this.produtoRepo = produtoRepo;
        this.produtoStockRepo = produtoStockRepo;
    }

    /**
     * Aplica um delta no stock:
     *  +n => Entrada
     *  -n => Saída
     *
     * Atualiza automaticamente:
     *  - produto.quantidade_exata (GLOBAL)
     *  - produto_stock.quantidade_exata (POR ARMAZÉM)
     */
    @Transactional
    public void aplicarDelta(String fkPortfolio, Integer fkArmazem, int delta)
    {
        if (fkPortfolio == null || fkPortfolio.isBlank())
        {
            throw new IllegalArgumentException("fkPortfolio é obrigatório");
        }
        if (fkArmazem == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório");
        }
        if (delta == 0)
        {
            return;
        }

        // 1) STOCK GLOBAL (produto)
        Produto produto = produtoRepo.findById(fkPortfolio)
            .orElseThrow(() -> new IllegalStateException(
                "Produto não encontrado para fkPortfolio: " + fkPortfolio
            ));

        int globalAtual = (produto.getQuantidadeExata() == null) ? 0 : produto.getQuantidadeExata();
        int globalDepois = globalAtual + delta;

        if (globalDepois < 0)
        {
            throw new IllegalStateException(
                "Stock global insuficiente. Produto=" + fkPortfolio
                    + " | Stock=" + globalAtual
                    + " | Delta=" + delta
            );
        }

        // 2) STOCK POR ARMAZÉM (produto_stock)
        ProdutoStockArmazem id = new ProdutoStockArmazem(fkPortfolio, fkArmazem);

        ProdutoStock ps = produtoStockRepo.findById(id)
            .orElseGet(() ->
            {
                ProdutoStock novo = new ProdutoStock();
                novo.setFkPortfolio(fkPortfolio);
                novo.setFkArmazem(fkArmazem);
                novo.setQuantidadeExata(0);
                return novo;
            });

        int armazemAtual = (ps.getQuantidadeExata() == null) ? 0 : ps.getQuantidadeExata();
        int armazemDepois = armazemAtual + delta;

        if (armazemDepois < 0)
        {
            throw new IllegalStateException(
                "Stock por armazém insuficiente. Produto=" + fkPortfolio
                    + " | Armazém=" + fkArmazem
                    + " | Stock=" + armazemAtual
                    + " | Delta=" + delta
            );
        }

        // 3) Persistir
        produto.setQuantidadeExata(globalDepois);
        produtoRepo.save(produto);

        ps.setQuantidadeExata(armazemDepois);
        produtoStockRepo.save(ps);
    }
}

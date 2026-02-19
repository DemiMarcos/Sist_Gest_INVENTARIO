package ucan.gestaoInventario.services.defesa;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucan.gestaoInventario.dto.rto.defesa.DefesaEncomendaGeradaRTO;
import ucan.gestaoInventario.dto.rto.defesa.DefesaGerarEncomendaDTO;
import ucan.gestaoInventario.dto.rto.defesa.DefesaProdutoPrecoStockRTO;
import ucan.gestaoInventario.entities.Encomenda;
import ucan.gestaoInventario.entities.EncomendaItem;
import ucan.gestaoInventario.entities.Pessoa;
import ucan.gestaoInventario.entities.Portfolio;
import ucan.gestaoInventario.repositories.EncomendaItemRepository;
import ucan.gestaoInventario.repositories.EncomendaRepository;
import ucan.gestaoInventario.repositories.PessoaRepository;
import ucan.gestaoInventario.repositories.PortfolioRepository;
import ucan.gestaoInventario.repositories.defesa.DefesaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefesaService
{

    private final DefesaRepository defesaRepository;
    private final EncomendaRepository encomendaRepository;
    private final EncomendaItemRepository encomendaItemRepository;
    private final PessoaRepository pessoaRepository;
    private final PortfolioRepository portfolioRepository;

    public DefesaService(
        DefesaRepository defesaRepository,
        EncomendaRepository encomendaRepository,
        EncomendaItemRepository encomendaItemRepository,
        PessoaRepository pessoaRepository,
        PortfolioRepository portfolioRepository
    )
    {
        this.defesaRepository = defesaRepository;
        this.encomendaRepository = encomendaRepository;
        this.encomendaItemRepository = encomendaItemRepository;
        this.pessoaRepository = pessoaRepository;
        this.portfolioRepository = portfolioRepository;
    }

    // 1) Listar preços + stock numa data (passada ou presente) + preço > X (precoMin)
    public List<DefesaProdutoPrecoStockRTO> listarPrecoStockNaData(
        Integer fkArmazem,
        LocalDateTime dataRef,
        Double precoMin,
        String prefixo
    )
    {
        if (fkArmazem == null)
        {
            throw new RuntimeException("fkArmazem é obrigatório");
        }
        if (dataRef == null)
        {
            throw new RuntimeException("dataRef é obrigatório");
        }
        if (precoMin == null)
        {
            throw new RuntimeException("precoMin é obrigatório");
        }

        String prefixoTratado = (prefixo == null || prefixo.isBlank()) ? null : prefixo.trim();

        return defesaRepository.listarPrecoStockNaData(
            fkArmazem,
            dataRef,
            precoMin,
            prefixoTratado
        );
    }

    // 2) Gerar encomenda automática com base nos filtros
    @Transactional
    public DefesaEncomendaGeradaRTO gerarEncomenda(DefesaGerarEncomendaDTO dto)
    {
        if (dto == null)
        {
            throw new RuntimeException("DTO é obrigatório");
        }
        if (dto.getFkArmazem() == null)
        {
            throw new RuntimeException("fkArmazem é obrigatório");
        }
        if (dto.getDataRef() == null)
        {
            throw new RuntimeException("dataRef é obrigatório");
        }
        if (dto.getPrecoMin() == null)
        {
            throw new RuntimeException("precoMin é obrigatório");
        }
        if (dto.getFkFornecedor() == null)
        {
            throw new RuntimeException("fkFornecedor é obrigatório");
        }

        // 2.1 Buscar fornecedor
        Pessoa fornecedor = pessoaRepository.buscar(dto.getFkFornecedor())
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado: " + dto.getFkFornecedor()));

        // 2.2 Buscar lista de produtos elegíveis (ordenada por descrição)
        List<DefesaProdutoPrecoStockRTO> produtos = listarPrecoStockNaData(
            dto.getFkArmazem(),
            dto.getDataRef(),
            dto.getPrecoMin(),
            dto.getPrefixo()
        );

        if (produtos.isEmpty())
        {
            throw new RuntimeException("Nenhum produto encontrado com os filtros informados.");
        }

        // 2.3 Criar encomenda
        Encomenda encomenda = new Encomenda();
        encomenda.setDataEncomenda(LocalDateTime.now());
        encomenda.setEstado("GERADA");
        encomenda.setFornecedor(fornecedor);

        Encomenda encomendaSalva = encomendaRepository.save(encomenda);

        // 2.4 Criar itens (quantidade padrão = 1)
        for (DefesaProdutoPrecoStockRTO r : produtos)
        {
            Portfolio portfolio = portfolioRepository.findById(r.getFkPortfolio())
                .orElseThrow(() -> new RuntimeException("Portfolio não encontrado: " + r.getFkPortfolio()));

            EncomendaItem item = new EncomendaItem();
            item.setEncomenda(encomendaSalva);
            item.setPortfolio(portfolio);
            item.setQuantidade(1);

            encomendaItemRepository.save(item);
        }

        // 2.5 Retornar RTO (COM 3 PARAMS — corrigindo teu erro do construtor)
        return new DefesaEncomendaGeradaRTO(
            encomendaSalva.getPkEncomenda(),
            produtos.size(),
            encomendaSalva.getDataEncomenda()
        );
    }
}

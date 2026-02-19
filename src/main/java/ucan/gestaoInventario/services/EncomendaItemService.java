package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ucan.gestaoInventario.dto.rto.EncomendaItemCreateDTO;
import ucan.gestaoInventario.dto.rto.EncomendaItemResponseRTO;
import ucan.gestaoInventario.entities.Encomenda;
import ucan.gestaoInventario.entities.EncomendaItem;
import ucan.gestaoInventario.entities.Portfolio;
import ucan.gestaoInventario.repositories.EncomendaItemRepository;
import ucan.gestaoInventario.repositories.EncomendaRepository;
import ucan.gestaoInventario.repositories.PortfolioRepository;

import java.util.List;

@Service
public class EncomendaItemService
{

    private final EncomendaRepository encomendaRepository;
    private final EncomendaItemRepository itemRepository;
    private final PortfolioRepository portfolioRepository;

    public EncomendaItemService(
        EncomendaRepository encomendaRepository,
        EncomendaItemRepository itemRepository,
        PortfolioRepository portfolioRepository
    )
    {
        this.encomendaRepository = encomendaRepository;
        this.itemRepository = itemRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public EncomendaItemResponseRTO adicionarItem(Integer idEncomenda, EncomendaItemCreateDTO dto)
    {
        if (idEncomenda == null)
        {
            throw new IllegalArgumentException("idEncomenda é obrigatório");
        }
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }

        Encomenda e = encomendaRepository.findById(idEncomenda)
            .orElseThrow(() -> new IllegalStateException("Encomenda não encontrada: " + idEncomenda));

        if (!"ABERTA".equalsIgnoreCase(e.getEstado()))
        {
            throw new IllegalStateException("Só é possível adicionar itens quando a encomenda está ABERTA");
        }

        String fk = trim(dto.getFkPortfolio());
        if (fk == null)
        {
            throw new IllegalArgumentException("fkPortfolio é obrigatório");
        }

        Portfolio p = portfolioRepository.findById(fk)
            .orElseThrow(() -> new IllegalStateException("Portfólio não encontrado: " + fk));

        if (dto.getQuantidade() == null || dto.getQuantidade() < 1)
        {
            throw new IllegalArgumentException("quantidade deve ser >= 1");
        }

        EncomendaItem it = new EncomendaItem();
        it.setEncomenda(e);
        it.setPortfolio(p);
        it.setQuantidade(dto.getQuantidade());

        it = itemRepository.save(it);

        return new EncomendaItemResponseRTO(
            it.getPkItem(),
            p.getPkPortfolio(),
            p.getDescricao(),
            it.getQuantidade()
        );
    }

    @Transactional(readOnly = true)
    public List<EncomendaItemResponseRTO> listarItens(Integer idEncomenda)
    {
        if (idEncomenda == null)
        {
            throw new IllegalArgumentException("idEncomenda é obrigatório");
        }

        if (encomendaRepository.findById(idEncomenda).isEmpty())
        {
            throw new IllegalStateException("Encomenda não encontrada: " + idEncomenda);
        }

        return itemRepository.listarItensDaEncomenda(idEncomenda)
            .stream()
            .map(it -> new EncomendaItemResponseRTO(
            it.getPkItem(),
            it.getPortfolio().getPkPortfolio(),
            it.getPortfolio().getDescricao(),
            it.getQuantidade()
        ))
            .toList();
    }

    @Transactional
    public void removerItem(Integer idEncomenda, Integer pkItem)
    {
        if (idEncomenda == null || pkItem == null)
        {
            throw new IllegalArgumentException("idEncomenda e pkItem são obrigatórios");
        }

        Encomenda e = encomendaRepository.findById(idEncomenda)
            .orElseThrow(() -> new IllegalStateException("Encomenda não encontrada: " + idEncomenda));

        if (!"ABERTA".equalsIgnoreCase(e.getEstado()))
        {
            throw new IllegalStateException("Só é possível remover itens quando a encomenda está ABERTA");
        }

        EncomendaItem it = itemRepository.findById(pkItem)
            .orElseThrow(() -> new IllegalStateException("Item não encontrado: " + pkItem));

        if (!it.getEncomenda().getPkEncomenda().equals(idEncomenda))
        {
            throw new IllegalStateException("Este item não pertence à encomenda " + idEncomenda);
        }

        itemRepository.delete(it);
    }

    private String trim(String s)
    {
        if (s == null)
        {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}

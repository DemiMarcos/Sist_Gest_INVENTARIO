package ucan.gestaoInventario.services;

import ucan.gestaoInventario.dto.rto.PortfolioNodeRTO;
import ucan.gestaoInventario.entities.Portfolio;
import ucan.gestaoInventario.repositories.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PortfolioTreeService
{

    private final PortfolioRepository portfolioRepository;

    public PortfolioTreeService(PortfolioRepository portfolioRepository)
    {
        this.portfolioRepository = portfolioRepository;
    }

    public List<PortfolioNodeRTO> obterArvore()
    {

        List<Portfolio> todos = portfolioRepository.findAll();

        Map<String, PortfolioNodeRTO> mapa = new HashMap<>();
        for (Portfolio p : todos)
        {
            mapa.put(
                p.getPkPortfolio(),
                new PortfolioNodeRTO(p.getPkPortfolio(), p.getDescricao())
            );
        }

        List<PortfolioNodeRTO> raizes = new ArrayList<>();

        for (Portfolio p : todos)
        {

            PortfolioNodeRTO atual = mapa.get(p.getPkPortfolio());

            if (p.getFkPortfolioPai() == null)
            {
                raizes.add(atual);
            }
            else
            {
                String pkPai = p.getFkPortfolioPai().getPkPortfolio();
                PortfolioNodeRTO pai = mapa.get(pkPai);

                // segurança extra
                if (pai != null)
                {
                    pai.getFilhos().add(atual);
                }
                else
                {
                    raizes.add(atual); // evita NullPointer caso BD esteja inconsistente
                }
            }
        }

        return raizes;
    }
}

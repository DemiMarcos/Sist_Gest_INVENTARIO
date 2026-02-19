package ucan.gestaoInventario.services;

import ucan.gestaoInventario.entities.Portfolio;
import ucan.gestaoInventario.repositories.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortfolioService
{

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository)
    {
        this.portfolioRepository = portfolioRepository;
    }

    public Portfolio gerarPortfolioPai(String pkPortfolio)
    {

        if (!pkPortfolio.contains("."))
        {
            return null;
        }

        int indice = pkPortfolio.lastIndexOf(".");

        String pkPai = pkPortfolio.substring(0, indice);

        Optional<Portfolio> pai = portfolioRepository.findById(pkPai);

        if (pai.isEmpty())
        {
            throw new RuntimeException(
                "Erro no portfólio: pai não encontrado para pk_portfolio = " + pkPortfolio
            );
        }

        return pai.get();
    }
}

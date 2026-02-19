package ucan.gestaoInventario.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ucan.gestaoInventario.dto.rto.ProdutoStockPrecoNaDataRTO;
import ucan.gestaoInventario.repositories.RelatorioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService
{

    private final RelatorioRepository relatorioRepository;

    public List<ProdutoStockPrecoNaDataRTO> listarProdutosStockPrecoPorGrupoNaData(
        String prefixo,
        LocalDateTime dataRef,
        Double precoMin
    )
    {
        if (prefixo == null || prefixo.isBlank())
        {
            throw new IllegalArgumentException("prefixo é obrigatório.");
        }
        if (dataRef == null)
        {
            throw new IllegalArgumentException("dataRef é obrigatória.");
        }
        if (precoMin == null)
        {
            precoMin = 0.0;
        }

        return relatorioRepository.listarProdutosStockPrecoPorGrupoNaData(prefixo.trim(), dataRef, precoMin);
    }
}

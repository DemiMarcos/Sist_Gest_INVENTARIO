package ucan.gestaoInventario.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ucan.gestaoInventario.dto.rto.LocalizacaoCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.LocalizacaoResponseRTO;
import ucan.gestaoInventario.dto.rto.LocalizacaoUpdateRequestDTO;
import ucan.gestaoInventario.entities.Armazem;
import ucan.gestaoInventario.entities.Localizacao;
import ucan.gestaoInventario.repositories.ArmazemRepository;
import ucan.gestaoInventario.repositories.LocalizacaoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalizacaoService
{

    private final LocalizacaoRepository localRepo;
    private final ArmazemRepository armazemRepo;

    @Transactional
    public LocalizacaoResponseRTO criar(Integer fkArmazem, LocalizacaoCreateRequestDTO dto)
    {
        if (fkArmazem == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório");
        }
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }

        Armazem armazem = armazemRepo.findById(fkArmazem)
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + fkArmazem));

        if (Boolean.FALSE.equals(armazem.getActivo()))
        {
            throw new IllegalStateException("Armazém está inactivo: " + armazem.getPkArmazem());
        }

        String codigo = trim(dto.getCodigo());
        if (codigo == null)
        {
            throw new IllegalArgumentException("codigo é obrigatório");
        }

        if (localRepo.findByArmazem_PkArmazemAndCodigoIgnoreCase(fkArmazem, codigo).isPresent())
        {
            throw new IllegalStateException("Já existe localização com este código neste armazém: " + codigo);
        }

        Localizacao l = new Localizacao();
        l.setArmazem(armazem);
        l.setCodigo(codigo);
        l.setDescricao(trim(dto.getDescricao()));
        l.setActivo(true);

        l = localRepo.save(l);
        return toRTO(l);
    }

    @Transactional(readOnly = true)
    public List<LocalizacaoResponseRTO> listarPorArmazem(Integer fkArmazem, Boolean soActivas)
    {
        if (fkArmazem == null)
        {
            throw new IllegalArgumentException("fkArmazem é obrigatório");
        }

        if (armazemRepo.findById(fkArmazem).isEmpty())
        {
            throw new IllegalStateException("Armazém não encontrado: " + fkArmazem);
        }

        List<Localizacao> lista = Boolean.TRUE.equals(soActivas)
            ? localRepo.listarActivasPorArmazem(fkArmazem)
            : localRepo.listarPorArmazem(fkArmazem);

        return lista.stream().map(this::toRTO).toList();
    }

    @Transactional(readOnly = true)
    public LocalizacaoResponseRTO detalhar(Integer fkArmazem, Integer pkLocalizacao)
    {
        if (fkArmazem == null || pkLocalizacao == null)
        {
            throw new IllegalArgumentException("fkArmazem e pkLocalizacao são obrigatórios");
        }

        Localizacao l = localRepo.findByArmazem_PkArmazemAndPkLocalizacao(fkArmazem, pkLocalizacao)
            .orElseThrow(() -> new IllegalStateException(
            "Localização não encontrada ou não pertence ao armazém. fkArmazem="
            + fkArmazem + " pkLocalizacao=" + pkLocalizacao
        ));

        return toRTO(l);
    }

    @Transactional
    public LocalizacaoResponseRTO atualizar(Integer fkArmazem, Integer pkLocalizacao, LocalizacaoUpdateRequestDTO dto)
    {
        if (fkArmazem == null || pkLocalizacao == null)
        {
            throw new IllegalArgumentException("fkArmazem e pkLocalizacao são obrigatórios");
        }
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }

        Localizacao l = localRepo.findByArmazem_PkArmazemAndPkLocalizacao(fkArmazem, pkLocalizacao)
            .orElseThrow(() -> new IllegalStateException(
            "Localização não encontrada ou não pertence ao armazém. fkArmazem="
            + fkArmazem + " pkLocalizacao=" + pkLocalizacao
        ));

        String novoCodigo = trim(dto.getCodigo());
        if (novoCodigo != null && !novoCodigo.equalsIgnoreCase(l.getCodigo()))
        {
            if (localRepo.findByArmazem_PkArmazemAndCodigoIgnoreCase(fkArmazem, novoCodigo).isPresent())
            {
                throw new IllegalStateException("Já existe localização com este código neste armazém: " + novoCodigo);
            }
            l.setCodigo(novoCodigo);
        }

        if (dto.getDescricao() != null)
        {
            l.setDescricao(trim(dto.getDescricao()));
        }

        if (dto.getActivo() != null)
        {
            l.setActivo(dto.getActivo());
        }

        l = localRepo.save(l);
        return toRTO(l);
    }

    @Transactional
    public void apagar(Integer fkArmazem, Integer pkLocalizacao)
    {
        if (fkArmazem == null || pkLocalizacao == null)
        {
            throw new IllegalArgumentException("fkArmazem e pkLocalizacao são obrigatórios");
        }

        Localizacao l = localRepo.findByArmazem_PkArmazemAndPkLocalizacao(fkArmazem, pkLocalizacao)
            .orElseThrow(() -> new IllegalStateException(
            "Localização não encontrada ou não pertence ao armazém. fkArmazem="
            + fkArmazem + " pkLocalizacao=" + pkLocalizacao
        ));

        localRepo.delete(l);
    }

    private LocalizacaoResponseRTO toRTO(Localizacao l)
    {
        Integer fkArmazem = (l.getArmazem() == null) ? null : l.getArmazem().getPkArmazem();
        String nomeArmazem = (l.getArmazem() == null) ? null : l.getArmazem().getNome();

        return new LocalizacaoResponseRTO(
            l.getPkLocalizacao(),
            fkArmazem,
            nomeArmazem,
            l.getCodigo(),
            l.getDescricao(),
            l.getActivo()
        );
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

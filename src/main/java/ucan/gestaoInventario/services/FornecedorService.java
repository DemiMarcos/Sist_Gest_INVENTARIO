package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import ucan.gestaoInventario.dto.rto.PessoaCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.PessoaResponseRTO;
import ucan.gestaoInventario.dto.rto.PessoaUpdateRequestDTO;
import ucan.gestaoInventario.repositories.EncomendaRepository;

import java.util.List;

@Service
public class FornecedorService
{
    private final PessoaBaseService base;
    private final EncomendaRepository encomendaRepository;

    public FornecedorService(PessoaBaseService base, EncomendaRepository encomendaRepository)
    {
        this.base = base;
        this.encomendaRepository = encomendaRepository;
    }

    public PessoaResponseRTO criar(PessoaCreateRequestDTO dto)
    {
        return base.criar(PessoaBaseService.TIPO_FORNECEDOR, dto);
    }

    public List<PessoaResponseRTO> listar()
    {
        return base.listar(PessoaBaseService.TIPO_FORNECEDOR);
    }

    public PessoaResponseRTO detalhar(Integer id)
    {
        return base.detalhar(PessoaBaseService.TIPO_FORNECEDOR, id);
    }

    public PessoaResponseRTO atualizar(Integer id, PessoaUpdateRequestDTO dto)
    {
        return base.atualizar(PessoaBaseService.TIPO_FORNECEDOR, id, dto);
    }

    public PessoaResponseRTO desativar(Integer id)
    {
        if (id == null) throw new IllegalArgumentException("id é obrigatório");

        boolean emUso = encomendaRepository.existsByFornecedor_PkPessoa(id);
        if (emUso)
        {
            throw new IllegalStateException("Não é possível desativar: FORNECEDOR está associado a encomendas (id=" + id + ")");
        }

        return base.desativar(PessoaBaseService.TIPO_FORNECEDOR, id);
    }
}

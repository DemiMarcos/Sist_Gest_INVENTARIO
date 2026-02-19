package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import ucan.gestaoInventario.dto.rto.PessoaCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.PessoaResponseRTO;
import ucan.gestaoInventario.dto.rto.PessoaUpdateRequestDTO;
import ucan.gestaoInventario.repositories.VendaRepository;

import java.util.List;

@Service
public class ClienteService
{

    private final PessoaBaseService base;
    private final VendaRepository vendaRepository;

    public ClienteService(PessoaBaseService base, VendaRepository vendaRepository)
    {
        this.base = base;
        this.vendaRepository = vendaRepository;
    }

    public PessoaResponseRTO criar(PessoaCreateRequestDTO dto)
    {
        return base.criar(PessoaBaseService.TIPO_CLIENTE, dto);
    }

    public List<PessoaResponseRTO> listar()
    {
        return base.listar(PessoaBaseService.TIPO_CLIENTE);
    }

    public PessoaResponseRTO detalhar(Integer id)
    {
        return base.detalhar(PessoaBaseService.TIPO_CLIENTE, id);
    }

    public PessoaResponseRTO atualizar(Integer id, PessoaUpdateRequestDTO dto)
    {
        return base.atualizar(PessoaBaseService.TIPO_CLIENTE, id, dto);
    }

    public PessoaResponseRTO desativar(Integer id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id é obrigatório");
        }

        boolean emUso = vendaRepository.existsByCliente_PkPessoa(id);
        if (emUso)
        {
            throw new IllegalStateException("Não é possível desativar: CLIENTE está associado a vendas (id=" + id + ")");
        }

        return base.desativar(PessoaBaseService.TIPO_CLIENTE, id);
    }
}

package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ucan.gestaoInventario.dto.rto.PessoaCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.PessoaResponseRTO;
import ucan.gestaoInventario.dto.rto.PessoaUpdateRequestDTO;
import ucan.gestaoInventario.entities.*;
import ucan.gestaoInventario.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaBaseService
{

    public static final String TIPO_FORNECEDOR = "FORNECEDOR";
    public static final String TIPO_CLIENTE = "CLIENTE";

    private final PessoaRepository pessoaRepository;
    private final PessoaTipoRepository pessoaTipoRepository;
    private final PessoaTipoMapRepository pessoaTipoMapRepository;

    private final EmailRepository emailRepository;
    private final TelefoneRepository telefoneRepository;
    private final PessoaEmailRepository pessoaEmailRepository;
    private final PessoaTelefoneRepository pessoaTelefoneRepository;

    public PessoaBaseService(
        PessoaRepository pessoaRepository,
        PessoaTipoRepository pessoaTipoRepository,
        PessoaTipoMapRepository pessoaTipoMapRepository,
        EmailRepository emailRepository,
        TelefoneRepository telefoneRepository,
        PessoaEmailRepository pessoaEmailRepository,
        PessoaTelefoneRepository pessoaTelefoneRepository
    )
    {
        this.pessoaRepository = pessoaRepository;
        this.pessoaTipoRepository = pessoaTipoRepository;
        this.pessoaTipoMapRepository = pessoaTipoMapRepository;
        this.emailRepository = emailRepository;
        this.telefoneRepository = telefoneRepository;
        this.pessoaEmailRepository = pessoaEmailRepository;
        this.pessoaTelefoneRepository = pessoaTelefoneRepository;
    }

    // =========================================================
    // CRUD POR TIPO
    // =========================================================
    @Transactional
    public PessoaResponseRTO criar(String tipo, PessoaCreateRequestDTO dto)
    {
        String nome = trim(dto == null ? null : dto.getNome());
        if (nome == null)
        {
            throw new IllegalArgumentException("nome é obrigatório");
        }

        ensureTipoExiste(tipo);

        Pessoa p = new Pessoa();
        p.setNome(nome);
        p.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        p = pessoaRepository.save(p);

        garantirTipoPessoa(p, tipo);

        // contactos
        if (dto.getEmails() != null)
        {
            substituirEmails(p, dto.getEmails());
        }
        if (dto.getTelefones() != null)
        {
            substituirTelefones(p, dto.getTelefones());
        }

        return montarRTO(p);
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseRTO> listar(String tipo)
    {
        ensureTipoExiste(tipo);
        List<Pessoa> lista = pessoaRepository.listarPorTipo(tipo);

        List<PessoaResponseRTO> out = new ArrayList<>();
        for (Pessoa p : lista)
        {
            out.add(montarRTO(p));
        }
        return out;
    }

    @Transactional(readOnly = true)
    public PessoaResponseRTO detalhar(String tipo, Integer id)
    {
        ensureTipoExiste(tipo);
        Pessoa p = pessoaRepository.buscar(id)
            .orElseThrow(() -> new IllegalStateException("Pessoa não encontrada: " + id));

        // garante que a pessoa tem aquele tipo
        if (!pessoaTipoMapRepository.existeTipo(id, tipo))
        {
            throw new IllegalStateException("Pessoa " + id + " não é do tipo " + tipo);
        }

        return montarRTO(p);
    }

    @Transactional
    public PessoaResponseRTO atualizar(String tipo, Integer id, PessoaUpdateRequestDTO dto)
    {
        ensureTipoExiste(tipo);

        Pessoa p = pessoaRepository.buscar(id)
            .orElseThrow(() -> new IllegalStateException("Pessoa não encontrada: " + id));

        if (!pessoaTipoMapRepository.existeTipo(id, tipo))
        {
            throw new IllegalStateException("Pessoa " + id + " não é do tipo " + tipo);
        }

        if (dto != null)
        {
            String nome = trim(dto.getNome());
            if (dto.getNome() != null && nome == null)
            {
                throw new IllegalArgumentException("nome não pode ser vazio");
            }
            if (nome != null)
            {
                p.setNome(nome);
            }

            if (dto.getActivo() != null)
            {
                p.setActivo(dto.getActivo());
            }

            if (dto.getEmails() != null)
            {
                substituirEmails(p, dto.getEmails());
            }
            if (dto.getTelefones() != null)
            {
                substituirTelefones(p, dto.getTelefones());
            }
        }

        p = pessoaRepository.save(p);
        return montarRTO(p);
    }

    // =========================================================
    // CONTACTOS
    // =========================================================
    private void substituirEmails(Pessoa p, List<String> emails)
    {
        pessoaEmailRepository.apagarPorPessoa(p.getPkPessoa());

        for (String raw : emails)
        {
            String e = normalizarEmail(raw);
            if (e == null)
            {
                continue;
            }

            Email ent = emailRepository.findByEmailIgnoreCase(e)
                .orElseGet(() ->
                {
                    Email novo = new Email();
                    novo.setEmail(e);
                    return emailRepository.save(novo);
                });

            PessoaEmail pe = new PessoaEmail();
            pe.setPessoa(p);
            pe.setEmail(ent);
            pessoaEmailRepository.save(pe);
        }
    }

    private void substituirTelefones(Pessoa p, List<String> telefones)
    {
        pessoaTelefoneRepository.apagarPorPessoa(p.getPkPessoa());

        for (String raw : telefones)
        {
            String t = normalizarTelefone(raw);
            if (t == null)
            {
                continue;
            }

            Telefone ent = telefoneRepository.findByNumero(t)
                .orElseGet(() ->
                {
                    Telefone novo = new Telefone();
                    novo.setNumero(t);
                    return telefoneRepository.save(novo);
                });

            PessoaTelefone pt = new PessoaTelefone();
            pt.setPessoa(p);
            pt.setTelefone(ent);
            pessoaTelefoneRepository.save(pt);
        }
    }

    // =========================================================
    // TIPOS
    // =========================================================
    private void ensureTipoExiste(String tipo)
    {
        String t = trim(tipo);
        if (t == null)
        {
            throw new IllegalArgumentException("tipo inválido");
        }

        if (pessoaTipoRepository.findById(t).isEmpty())
        {
            PessoaTipo novo = new PessoaTipo();
            novo.setPkTipo(t);
            pessoaTipoRepository.save(novo);
        }
    }

    private void garantirTipoPessoa(Pessoa p, String tipo)
    {
        if (pessoaTipoMapRepository.existeTipo(p.getPkPessoa(), tipo))
        {
            return;
        }

        PessoaTipo tipoEnt = pessoaTipoRepository.findById(tipo)
            .orElseThrow(() -> new IllegalStateException("Tipo não existe: " + tipo));

        PessoaTipoMap map = new PessoaTipoMap();
        map.setPessoa(p);
        map.setTipo(tipoEnt);
        pessoaTipoMapRepository.save(map);
    }

    // =========================================================
    // RTO
    // =========================================================
    private PessoaResponseRTO montarRTO(Pessoa p)
    {
        Integer id = p.getPkPessoa();

        List<String> tipos = pessoaTipoMapRepository.listarTipos(id);
        List<String> emails = pessoaEmailRepository.listarEmails(id);
        List<String> telefones = pessoaTelefoneRepository.listarTelefones(id);

        return new PessoaResponseRTO(
            id,
            p.getNome(),
            p.getActivo(),
            tipos,
            emails,
            telefones
        );
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private String trim(String s)
    {
        if (s == null)
        {
            return null;
        }
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private String normalizarEmail(String s)
    {
        s = trim(s);
        if (s == null)
        {
            return null;
        }
        return s.toLowerCase();
    }

    private String normalizarTelefone(String s)
    {
        s = trim(s);
        if (s == null)
        {
            return null;
        }
        // remove espaços (podes melhorar se quiseres)
        s = s.replace(" ", "");
        return s.isEmpty() ? null : s;
    }

    // =========================================================
    // DELETE LÓGICO (activo=false)
    // =========================================================
    @Transactional
    public PessoaResponseRTO desativar(String tipo, Integer id)
    {
        ensureTipoExiste(tipo);

        if (id == null)
        {
            throw new IllegalArgumentException("id é obrigatório");
        }

        Pessoa p = pessoaRepository.buscar(id)
            .orElseThrow(() -> new IllegalStateException("Pessoa não encontrada: " + id));

        if (!pessoaTipoMapRepository.existeTipo(id, tipo))
        {
            throw new IllegalStateException("Pessoa " + id + " não é do tipo " + tipo);
        }

        if (Boolean.FALSE.equals(p.getActivo()))
        {
            return montarRTO(p); // já está desativada
        }

        p.setActivo(false);
        p = pessoaRepository.save(p);
        return montarRTO(p);
    }

}

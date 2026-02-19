package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucan.gestaoInventario.dto.rto.ArmazemCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.ArmazemResponseRTO;
import ucan.gestaoInventario.dto.rto.ArmazemUpdateRequestDTO;
import ucan.gestaoInventario.entities.Armazem;
import ucan.gestaoInventario.repositories.ArmazemRepository;

import java.util.List;

@Service
public class ArmazemService
{

    private final ArmazemRepository repo;

    public ArmazemService(ArmazemRepository repo)
    {
        this.repo = repo;
    }

    @Transactional
    public ArmazemResponseRTO criar(ArmazemCreateRequestDTO dto)
    {
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }

        String nome = trim(dto.getNome());
        if (nome == null)
        {
            throw new IllegalArgumentException("nome é obrigatório");
        }

        if (repo.findByNomeIgnoreCase(nome).isPresent())
        {
            throw new IllegalStateException("Já existe armazém com este nome: " + nome);
        }

        Armazem a = new Armazem();
        a.setNome(nome);
        a.setActivo(true);

        a = repo.save(a);
        return toRTO(a);
    }

    @Transactional(readOnly = true)
    public List<ArmazemResponseRTO> listar()
    {
        return repo.findAll()
            .stream()
            .map(this::toRTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public ArmazemResponseRTO detalhar(Integer id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id é obrigatório");
        }

        Armazem a = repo.findById(id)
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + id));

        return toRTO(a);
    }

    @Transactional
    public ArmazemResponseRTO atualizar(Integer id, ArmazemUpdateRequestDTO dto)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("id é obrigatório");
        }
        if (dto == null)
        {
            throw new IllegalArgumentException("body é obrigatório");
        }

        Armazem a = repo.findById(id)
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + id));

        String nome = trim(dto.getNome());
        if (nome == null)
        {
            throw new IllegalArgumentException("nome é obrigatório");
        }

        repo.findByNomeIgnoreCase(nome).ifPresent(outro ->
        {
            if (!outro.getPkArmazem().equals(id))
            {
                throw new IllegalStateException("Já existe armazém com este nome: " + nome);
            }
        });

        a.setNome(nome);
        a = repo.save(a);

        return toRTO(a);
    }

    @Transactional
    public ArmazemResponseRTO ativar(Integer id)
    {
        Armazem a = repo.findById(id)
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + id));

        a.setActivo(true);
        a = repo.save(a);
        return toRTO(a);
    }

    @Transactional
    public ArmazemResponseRTO desativar(Integer id)
    {
        Armazem a = repo.findById(id)
            .orElseThrow(() -> new IllegalStateException("Armazém não encontrado: " + id));

        a.setActivo(false);
        a = repo.save(a);
        return toRTO(a);
    }

    private ArmazemResponseRTO toRTO(Armazem a)
    {
        return new ArmazemResponseRTO(a.getPkArmazem(), a.getNome(), a.getActivo());
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

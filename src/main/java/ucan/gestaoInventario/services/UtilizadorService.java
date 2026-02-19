package ucan.gestaoInventario.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucan.gestaoInventario.entities.Utilizador;
import ucan.gestaoInventario.repositories.UtilizadorRepository;
import java.util.List;
import ucan.gestaoInventario.dto.rto.UtilizadorCreateRequestDTO;
import ucan.gestaoInventario.dto.rto.UtilizadorResponseRTO;

@Service
public class UtilizadorService
{

    private final UtilizadorRepository repository;

    public UtilizadorService(UtilizadorRepository repository)
    {
        this.repository = repository;
    }

    @Transactional
    public UtilizadorResponseRTO criar(UtilizadorCreateRequestDTO dto)
    {
        String username = normalizarUsername(dto.getUsername());
        validarUsernameDuplicado(username);

        Utilizador u = new Utilizador();
        u.setUsername(username);
        u.setActivo(true);

        Utilizador saved = repository.save(u);
        return toRTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UtilizadorResponseRTO> listar()
    {
        return repository.findAll().stream()
            .map(this::toRTO)
            .toList();
    }

    @Transactional
    public UtilizadorResponseRTO activar(Integer id)
    {
        Utilizador u = getById(id);
        u.setActivo(true);
        return toRTO(repository.save(u));
    }

    @Transactional
    public UtilizadorResponseRTO desactivar(Integer id)
    {
        Utilizador u = getById(id);
        u.setActivo(false);
        return toRTO(repository.save(u));
    }

    // =========================
    // Helpers (código limpo)
    // =========================
    private Utilizador getById(Integer id)
    {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Utilizador não encontrado: " + id));
    }

    private String normalizarUsername(String s)
    {
        if (s == null)
        {
            return null;
        }
        String x = s.trim();
        return x.isEmpty() ? null : x;
    }

    private void validarUsernameDuplicado(String username)
    {
        if (username == null)
        {
            throw new IllegalStateException("username é obrigatório");
        }

        boolean existe = repository.existsByUsernameIgnoreCase(username);
        if (existe)
        {
            throw new IllegalStateException("username já existe: " + username);
        }
    }

    private UtilizadorResponseRTO toRTO(Utilizador u)
    {
        return new UtilizadorResponseRTO(
            u.getPkUtilizador(),
            u.getUsername(),
            u.getActivo()
        );
    }
}

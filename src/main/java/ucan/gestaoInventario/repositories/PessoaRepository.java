package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer>
{

    @Query("""
        select p
        from Pessoa p
        join PessoaTipoMap m on m.pessoa.pkPessoa = p.pkPessoa
        where m.tipo.pkTipo = :tipo
        order by p.pkPessoa
    """)
    List<Pessoa> listarPorTipo(@Param("tipo") String tipo);

    @Query("""
        select p
        from Pessoa p
        where p.pkPessoa = :id
    """)
    Optional<Pessoa> buscar(@Param("id") Integer id);
}

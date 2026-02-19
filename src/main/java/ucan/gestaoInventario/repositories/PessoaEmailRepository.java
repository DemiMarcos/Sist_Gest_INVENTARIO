package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ucan.gestaoInventario.entities.PessoaEmail;

import java.util.List;

public interface PessoaEmailRepository extends JpaRepository<PessoaEmail, Integer>
{

    @Query("""
        select e.email
        from PessoaEmail pe
        join pe.email e
        where pe.pessoa.pkPessoa = :idPessoa
        order by lower(e.email)
    """)
    List<String> listarEmails(@org.springframework.data.repository.query.Param("idPessoa") Integer idPessoa);

    @Modifying
    @Query("delete from PessoaEmail pe where pe.pessoa.pkPessoa = :idPessoa")
    void apagarPorPessoa(@org.springframework.data.repository.query.Param("idPessoa") Integer idPessoa);
}

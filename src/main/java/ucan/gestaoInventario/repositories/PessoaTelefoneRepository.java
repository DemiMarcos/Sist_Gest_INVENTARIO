package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ucan.gestaoInventario.entities.PessoaTelefone;

import java.util.List;

public interface PessoaTelefoneRepository extends JpaRepository<PessoaTelefone, Integer>
{

    @Query("""
        select t.numero
        from PessoaTelefone pt
        join pt.telefone t
        where pt.pessoa.pkPessoa = :idPessoa
        order by t.numero
    """)
    List<String> listarTelefones(@org.springframework.data.repository.query.Param("idPessoa") Integer idPessoa);

    @Modifying
    @Query("delete from PessoaTelefone pt where pt.pessoa.pkPessoa = :idPessoa")
    void apagarPorPessoa(@org.springframework.data.repository.query.Param("idPessoa") Integer idPessoa);
}

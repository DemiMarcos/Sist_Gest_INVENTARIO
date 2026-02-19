package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ucan.gestaoInventario.entities.PessoaTipoMap;

import java.util.List;

public interface PessoaTipoMapRepository extends JpaRepository<PessoaTipoMap, Integer>
{

    @Query("""
        select m.tipo.pkTipo
        from PessoaTipoMap m
        where m.pessoa.pkPessoa = :idPessoa
        order by m.tipo.pkTipo
    """)
    List<String> listarTipos(@org.springframework.data.repository.query.Param("idPessoa") Integer idPessoa);

    @Query("""
        select count(m) > 0
        from PessoaTipoMap m
        where m.pessoa.pkPessoa = :idPessoa and m.tipo.pkTipo = :tipo
    """)
    boolean existeTipo(
        @org.springframework.data.repository.query.Param("idPessoa") Integer idPessoa,
        @org.springframework.data.repository.query.Param("tipo") String tipo
    );

    @Modifying
    @Query("delete from PessoaTipoMap m where m.pessoa.pkPessoa = :idPessoa and m.tipo.pkTipo = :tipo")
    void removerTipo(
        @org.springframework.data.repository.query.Param("idPessoa") Integer idPessoa,
        @org.springframework.data.repository.query.Param("tipo") String tipo
    );
}

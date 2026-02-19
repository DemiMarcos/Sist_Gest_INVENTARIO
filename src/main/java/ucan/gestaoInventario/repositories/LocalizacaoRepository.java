package ucan.gestaoInventario.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.Localizacao;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Integer>
{

    Optional<Localizacao> findByArmazem_PkArmazemAndPkLocalizacao(Integer fkArmazem, Integer pkLocalizacao);

    Optional<Localizacao> findByArmazem_PkArmazemAndCodigoIgnoreCase(Integer fkArmazem, String codigo);

    @Query("""
        select l
        from Localizacao l
        join fetch l.armazem a
        where a.pkArmazem = :fkArmazem
        order by l.codigo asc, l.pkLocalizacao asc
    """)
    List<Localizacao> listarPorArmazem(@Param("fkArmazem") Integer fkArmazem);

    @Query("""
        select l
        from Localizacao l
        join fetch l.armazem a
        where a.pkArmazem = :fkArmazem
          and l.activo = true
        order by l.codigo asc, l.pkLocalizacao asc
    """)
    List<Localizacao> listarActivasPorArmazem(@Param("fkArmazem") Integer fkArmazem);
}

package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.Telefone;

import java.util.Optional;

public interface TelefoneRepository extends JpaRepository<Telefone, Integer>
{

    @Query("select t from Telefone t where t.numero = :numero")
    Optional<Telefone> findByNumero(@Param("numero") String numero);
}

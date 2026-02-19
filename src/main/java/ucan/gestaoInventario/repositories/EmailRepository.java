package ucan.gestaoInventario.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ucan.gestaoInventario.entities.Email;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Integer>
{

    @Query("select e from Email e where lower(e.email) = lower(:email)")
    Optional<Email> findByEmailIgnoreCase(@Param("email") String email);
}

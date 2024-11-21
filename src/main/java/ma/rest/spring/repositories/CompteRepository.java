package ma.rest.spring.repositories;

import ma.rest.spring.entities.Compte;
import ma.rest.spring.entities.TypeCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    List<Compte> findByType(TypeCompte type);

    @Query("SELECT SUM(c.solde) FROM Compte c")
    double sumSoldes();
}

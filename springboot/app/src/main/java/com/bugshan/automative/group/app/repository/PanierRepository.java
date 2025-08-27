package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.Panier;
import com.bugshan.automative.group.app.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PanierRepository extends JpaRepository<Panier, Long> {

    Optional<Panier> findByUtilisateurAndConfirmeFalse(Utilisateur utilisateur);

    boolean existsByUtilisateurAndConfirmeFalse(Utilisateur utilisateur);

    Optional<Panier> findByUtilisateurIdAndConfirmeFalse(Long utilisateurId);

    @Query("SELECT p FROM Panier p LEFT JOIN FETCH p.lignes WHERE p.id = :id")
    Optional<Panier> findByIdWithLignes(@Param("id") Long id);

}

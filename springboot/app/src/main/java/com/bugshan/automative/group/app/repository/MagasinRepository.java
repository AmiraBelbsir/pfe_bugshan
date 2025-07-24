package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.model.Magasin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository


public interface MagasinRepository extends JpaRepository<Magasin, Long> {

    // Récupérer tous les magasins actifs (active = true)
    List<Magasin> findByActifTrue();

    // Rechercher magasins par nom partiel (ignore la casse)
    List<Magasin> findAllByNomContainingIgnoreCase(String nom);

    // Vérifier si un magasin existe avec un nom donné (exact)
    boolean existsByNom(String nom);

    // Trouver un magasin par nom exact
    Optional<Magasin> findByNom(String nom);

}


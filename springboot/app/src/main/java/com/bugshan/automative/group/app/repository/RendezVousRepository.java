package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    // Ici tu peux ajouter des méthodes personnalisées si besoin (ex: findByCommercialId)
}

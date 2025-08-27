package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.model.Utilisateur;
import com.bugshan.automative.group.app.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByClientId(Long clientId);

    boolean existsByClientAndVehiculeAndStatutIn(Utilisateur client, Vehicule vehicule, List<RendezVous.StatutRdv> enAttente);
    // Ici tu peux ajouter des méthodes personnalisées si besoin (ex: findByCommercialId)
}

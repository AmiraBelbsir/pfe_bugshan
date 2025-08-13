package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    // Par exemple, chercher par statut ou par commercial
    List<RendezVous> findByStatut(RendezVous.StatutRdv statut);
    List<RendezVous> findByCommercial_Id(Long commercialId);
    List<RendezVous> findByClient_Id(Long clientId);
}

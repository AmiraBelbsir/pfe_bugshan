package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.model.RendezVous.StatutRdv;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVousDTO {

    private Long id;
    private LocalDate date;
    private LocalTime heure;
    private String clientNom;
    private String vehiculeNom;
    private Long commercialId;
    private String commercialNom;
    private StatutRdv statut;

    // Constructeur à partir de l'entité
    public RendezVousDTO(RendezVous rdv) {
        this.id = rdv.getId();
        this.date = rdv.getDate();
        this.heure = rdv.getHeure();
        this.clientNom = rdv.getClientNom();
        this.vehiculeNom = rdv.getVehiculeNom();
        this.commercialId = rdv.getCommercialId();
        this.commercialNom = rdv.getCommercialNom();
        this.statut = rdv.getStatut();
    }
}

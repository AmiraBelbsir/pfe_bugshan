package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.Avis;
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
    private String date; // ou LocalDate selon sérialisation
    private String heure;
    private Long clientId;
    private String clientFullName;
    private Long commercialId;
    private String commercialFullName;
    private String commercialImage;
    private Long vehiculeId;
    private String vehiculeImage;
    private String vehiculeMakeModel; // ou autre représentation
    private String statut;
    private Long magasinId;
    private String magasinNom;
    private String magasinAdresse;
    private String avisCommentaire;
    private Integer avisNote;
    private AvisDTO avis;

    public RendezVousDTO(RendezVous rdv) {
        this.id = rdv.getId();
        this.date = rdv.getDate().toString();
        this.heure = rdv.getHeure().toString();
        if (rdv.getClient() != null) {
            this.clientId = rdv.getClient().getId();
            this.clientFullName = rdv.getClient().getNomComplet();
        }
        if (rdv.getMagasin() != null) {
            this.magasinId = rdv.getMagasin().getId();
            this.magasinNom = rdv.getMagasin().getNom();
            this.magasinAdresse = rdv.getMagasin().getAdresse();
        }


        if (rdv.getCommercial() != null) {
            this.commercialId = rdv.getCommercial().getId();
            this.commercialFullName = rdv.getCommercial().getNomComplet();
            this.commercialImage=rdv.getCommercial().getUrlImage();
        }
        if (rdv.getVehicule() != null) {
            this.vehiculeId = rdv.getVehicule().getId();
            this.vehiculeMakeModel = rdv.getVehicule().getMarque() + " " + rdv.getVehicule().getModele(); // adapte selon ton modèle
            this.vehiculeImage = rdv.getVehicule().getUrlImage(); // adapte selon ton modèle
        }
        if (rdv.getAvis() != null) {
            this.avisCommentaire = rdv.getAvis().getCommentaire();
            this.avisNote = rdv.getAvis().getNote();
            this.avis = new AvisDTO(rdv.getAvis()); // <-- corrige ici
        }


        this.statut = rdv.getStatut().name();
    }


}
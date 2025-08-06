package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.Avis;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public class AvisDTO {

    private Long id;
    private String commentaire;
    private int note;
    private LocalDateTime dateCreation;
    private Long utilisateurId;
    private String utilisateurNom;
    private Long vehiculeId;
    private String vehiculeMarque;
    private String vehiculeModele;

    public AvisDTO() {}

    public AvisDTO(Long id, String commentaire, int note, LocalDateTime dateCreation, Long utilisateurId,String utilisateurNom, String vehiculeMarque,String vehiculeModele, Long vehiculeId) {
        this.id = id;
        this.commentaire = commentaire;
        this.note = note;
        this.dateCreation = dateCreation;
        this.utilisateurId = utilisateurId;
        this.utilisateurNom = utilisateurNom;
        this.vehiculeId = vehiculeId;
        this.vehiculeMarque = vehiculeMarque;
        this.vehiculeModele = vehiculeModele;
    }

    public AvisDTO(Avis avis) {
        this.id = avis.getId();
        this.commentaire = avis.getCommentaire();
        this.note = avis.getNote();
        this.dateCreation = avis.getDateCreation();
        this.utilisateurId = avis.getUtilisateur() != null ? avis.getUtilisateur().getId() : null;
        this.utilisateurNom = avis.getUtilisateur().getNomComplet();
        this.vehiculeMarque = avis.getVehicule().getMarque();
        this.vehiculeModele = avis.getVehicule().getModele();
        this.vehiculeId = avis.getVehicule() != null ? avis.getVehicule().getId() : null;
    }


}

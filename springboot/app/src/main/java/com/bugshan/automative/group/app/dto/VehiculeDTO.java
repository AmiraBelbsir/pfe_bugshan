package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculeDTO {

    private Long id;
    private String urlImage;                // URL de l'image principale
    private String marque;                  // Marque du véhicule
    private String modele;                  // Modèle du véhicule
    private int annee;                      // Année de fabrication
    private String couleur;                 // Couleur du véhicule
    private int sieges;                     // Nombre de sièges
    private TypeVehicule typeVehicule;      // Type de véhicule
    private double prix;                    // Prix par jour
    private boolean disponible;             // Disponibilité
    private String emplacement;             // Emplacement GPS
    private double niveauCarburant;         // Niveau de carburant
    private TypeCarburant typeCarburant;    // Type de carburant
    private TypeTransmission typeTransmission; // Type de transmission
    private List<PhotoVehiculeDTO> photosAdditionnelles; // Liste des photos supplémentaires

    // Constructeur personnalisé pour transformer une entité Vehicule en DTO
    public VehiculeDTO(Vehicule vehicule) {
        this.id = vehicule.getId();
        this.urlImage = vehicule.getUrlImage();
        this.marque = vehicule.getMarque();
        this.modele = vehicule.getModele();
        this.annee = vehicule.getAnnee();
        this.couleur = vehicule.getCouleur();
        this.sieges = vehicule.getSieges();
        this.typeVehicule = vehicule.getTypeVehicule();
        this.prix = vehicule.getPrix();
        this.disponible = vehicule.isDisponible();
        this.emplacement = vehicule.getEmplacement();
        this.niveauCarburant = vehicule.getNiveauCarburant();
        this.typeCarburant = vehicule.getTypeCarburant();
        this.typeTransmission = vehicule.getTypeTransmission();

        // Conversion des photos supplémentaires en DTO
        this.photosAdditionnelles = vehicule.getPhotosAdditionnelles().stream()
                .map(PhotoVehiculeDTO::new)
                .collect(Collectors.toList());
    }
}

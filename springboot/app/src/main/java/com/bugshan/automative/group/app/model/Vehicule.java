package com.bugshan.automative.group.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String urlImage;           // Image principale du véhicule
    private String marque;             // Marque du véhicule
    private String modele;             // Modèle du véhicule
    private int quantite;
    private int annee;                 // Année de fabrication
    private String couleur;            // Couleur du véhicule
    private int sieges;                // Nombre de sièges
    private double prix;               // Prix à la journée
    private boolean disponible = true; // Disponibilité
    private String emplacement;        // Emplacement GPS
    private double niveauCarburant;    // Niveau de carburant

    @Enumerated(EnumType.STRING)
    private TypeVehicule typeVehicule;  // Type (SUV, Berline, etc.)

    @Enumerated(EnumType.STRING)
    private TypeCarburant typeCarburant;    // Type de carburant

    @Enumerated(EnumType.STRING)
    private TypeTransmission typeTransmission;  // Type de transmission

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicle_additional_photos", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "photo_url")
    @JsonProperty("photosAdditionnelles")
    private List<String> photosAdditionnelles = new ArrayList<>();

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("vehicle")
    private List<Avis> avis;

    @ManyToMany(mappedBy = "vehiculesCompatibles", fetch = FetchType.EAGER)
    private List<Piece> piecesCompatibles;
}

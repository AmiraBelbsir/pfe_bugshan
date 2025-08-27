package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class PieceDTO {
    private Long id;
    private String nom;
    private String reference;
    private String marque;
    private double prix;
    private int quantite;
    private int quantiteMinimum;
    private String imageUrl;
    private TypePiece type;
    private LocalDate dateAchat;
    private String description;
    private boolean precommandable;
    private boolean active;

    // Magasin + Bloc
    private Long magasinId;
    private String magasinNom;
    private Long blocId;
    private String blocNom;

    // Fournisseur (Utilisateur)
    private Long fournisseurId;
    private String fournisseurNom;

    // Véhicules compatibles
    private List<Long> vehiculeIds;
    private List<String> vehiculeModeles; // <- Ajouté

    // Constructeur à partir de l’entité Piece
    public PieceDTO(Piece piece) {
        this.id = piece.getId();
        this.nom = piece.getNom();
        this.reference = piece.getReference();
        this.marque = piece.getMarque();
        this.prix = piece.getPrix();
        this.quantite = piece.getQuantite();
        this.quantiteMinimum = piece.getQuantiteMinimum();
        this.imageUrl = piece.getImageUrl();
        this.type = piece.getType();
        this.dateAchat = piece.getDateAchat();
        this.description = piece.getDescription();
        this.precommandable = piece.isPrecommandable();
        this.active = piece.isActive();

        // Magasin
        if (piece.getMagasin() != null) {
            this.magasinId = piece.getMagasin().getId();
            this.magasinNom = piece.getMagasin().getNom();
        }

        // Bloc
        if (piece.getBloc() != null) {
            this.blocId = piece.getBloc().getId();
            this.blocNom = piece.getBloc().getNom();
        }

        // Fournisseur
        if (piece.getFournisseur() != null) {
            this.fournisseurId = piece.getFournisseur().getId();
            this.fournisseurNom = piece.getFournisseur().getNomComplet();
        }

        // Véhicules compatibles
        if (piece.getVehiculesCompatibles() != null) {
            this.vehiculeIds = piece.getVehiculesCompatibles()
                    .stream()
                    .map(Vehicule::getId)
                    .collect(Collectors.toList());

            this.vehiculeModeles = piece.getVehiculesCompatibles()
                    .stream()
                    .map(v -> v.getMarque() + " " + v.getModele())
                    .collect(Collectors.toList());
        }
    }

    public PieceDTO(org.springframework.beans.PropertyValue propertyValue) {
        // À supprimer si inutile
    }
}

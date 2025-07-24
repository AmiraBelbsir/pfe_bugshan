package com.bugshan.automative.group.app.dto;


import com.bugshan.automative.group.app.model.*;
import lombok.*;
import org.springframework.beans.PropertyValue;

import java.time.LocalDate;
import java.util.Date;

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
    private String imageUrl;
    private TypePiece type;
    private LocalDate DateAchat;
    private int quantiteMinimum;
    private String description;
    private String compatibilite;
    private boolean precommandable;
    private boolean active;
    private Long magasinId;
    private Long blocId;
    private String magasinNom;
    private String blocNom;




    // Constructeur
    public PieceDTO(Piece piece) {
        this.id = piece.getId();
        this.nom = piece.getNom();
        this.reference = piece.getReference();
        this.marque = piece.getMarque();
        this.prix = piece.getPrix();
        this.quantite = piece.getQuantite();
        this.imageUrl = piece.getImageUrl();
        this.type = piece.getType();
        this.DateAchat = piece.getDateAchat();
        this.quantiteMinimum = piece.getQuantiteMinimum();
        this.description = piece.getDescription();
        this.compatibilite = piece.getCompatibilite();
        this.precommandable = piece.isPrecommandable();
        this.active = piece.isActive();
        // ✅ Récupérer les ID du magasin et du bloc depuis l'entité
        this.magasinId = (piece.getMagasin() != null) ? piece.getMagasin().getId() : null;
        this.blocId = (piece.getBloc() != null) ? piece.getBloc().getId() : null;
        this.magasinNom = piece.getMagasin().getNom();
        this.blocNom = piece.getBloc().getNom();
    }

    public PieceDTO(PropertyValue propertyValue) {
    }
}
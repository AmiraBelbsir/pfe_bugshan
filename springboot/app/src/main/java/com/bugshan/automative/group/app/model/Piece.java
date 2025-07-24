package com.bugshan.automative.group.app.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Piece {
    @Id // identifiant unique
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrémenté
    private Long id;

    private String nom;
    private String reference;
    private String marque;
    private double prix;
    private int quantite;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private TypePiece type;
    private LocalDate dateAchat;
    private int quantiteMinimum;
    @Column(length = 1000) // pour éviter une limite trop courte
    private String description;
    private String compatibilite;
    private boolean precommandable;
    private boolean active=true;

    @ManyToOne
    @JoinColumn(name = "magasin_id")// clé étrangère dans la table Piece
    private Magasin magasin;

    @ManyToOne
    @JoinColumn(name = "bloc_id")// clé étrangère dans la table Piece
    private Bloc bloc;

}
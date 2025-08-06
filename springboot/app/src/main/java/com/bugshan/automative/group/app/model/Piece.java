package com.bugshan.automative.group.app.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String reference;

    private String marque;

    private double prix;

    private int quantite;

    private int quantiteMinimum;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private TypePiece type;

    private LocalDate dateAchat;

    @Column(length = 1000)
    private String description;

    private boolean precommandable;

    private boolean active = true;

    @ManyToOne
    @JsonBackReference("magasin-piece")
    @JoinColumn(name = "magasin_id")
    private Magasin magasin;

    @ManyToOne
    @JsonBackReference("bloc-piece")
    @JoinColumn(name = "bloc_id")
    private Bloc bloc;

    @ManyToOne
    @JsonBackReference("fournisseur-piece")
    @JoinColumn(name = "fournisseur_id")
    private Utilisateur fournisseur;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "piece_vehicule",
            joinColumns = @JoinColumn(name = "piece_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicule_id")
    )
    private List<Vehicule> vehiculesCompatibles;
}

package com.bugshan.automative.group.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime heure;

    // Relation vers l'avis'
    @OneToOne(mappedBy = "rdv", cascade = CascadeType.ALL)
    private Avis avis;

    // Relation vers le magasin
    @ManyToOne
    @JoinColumn(name = "magasin_id") // nom de la colonne dans la table rendez_vous
    @JsonBackReference("magasin-rdv")
    private Magasin magasin;

    // Relation vers le client (utilisateur)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Utilisateur client;

    // Relation vers le commercial (utilisateur)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commercial_id")
    private Utilisateur commercial;

    // Relation vers le véhicule
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @Enumerated(EnumType.STRING)
    private StatutRdv statut;

    // Constructeur par défaut avec statut initial
    public RendezVous() {
        this.statut = StatutRdv.EN_ATTENTE;
    }

    // enum pour statut
    public enum StatutRdv {
        EN_ATTENTE,
        VALIDE,
        REFUSE,
        TERMINE
    }
}


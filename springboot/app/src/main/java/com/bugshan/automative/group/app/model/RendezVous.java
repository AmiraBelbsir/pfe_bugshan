package com.bugshan.automative.group.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime heure;

    private String clientNom;

    private String vehiculeNom;

    private Long commercialId;  // Id du commercial associé

    private String commercialNom;

    @Enumerated(EnumType.STRING)
    private StatutRdv statut;

    // Constructeurs, getters, setters

    public RendezVous() {}

    // getters et setters pour chaque attribut...

    // enum pour statut
    public enum StatutRdv {
        EN_ATTENTE,
        VALIDE,
        REFUSE,
        TERMINE
    }
}

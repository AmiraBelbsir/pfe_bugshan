package com.bugshan.automative.group.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String commentaire;

    @Column(nullable = false)
    private int note;  // Entre 1 et 5

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference("user")
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference("vehicle")
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;
}

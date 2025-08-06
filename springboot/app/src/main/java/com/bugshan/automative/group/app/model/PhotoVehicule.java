package com.bugshan.automative.group.app.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "photos_vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;   // Référence au véhicule associé à cette photo

    @Column(name = "url_photo")
    private String urlPhoto;     // URL ou chemin de la photo
}

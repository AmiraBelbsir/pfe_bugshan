package com.bugshan.automative.group.app.model;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle_photos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiclePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;   // Référence au véhicule associé à cette photo

    private String photoUrl;   // URL ou chemin de la photo
}

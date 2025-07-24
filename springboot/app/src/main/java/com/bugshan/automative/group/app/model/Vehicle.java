package com.bugshan.automative.group.app.model;

import lombok.*;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;           // Image principale du véhicule
    private String make;               // Marque du véhicule
    private String model;              // Modèle du véhicule
    private int quantity;
    private int year;                  // Année de fabrication
    private String color;              // Couleur du véhicule
    private int seats;                 // Nombre de sièges
    private double retailPrice;        // Prix par jour
    private boolean available = true;  // Disponibilité du véhicule
    private String location;           // Emplacement actuel (coordonnées GPS)
    private double fuelLevel;          // Niveau de carburant

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;  // Type de véhicule (par exemple, berline, SUV, etc.)


    @Enumerated(EnumType.STRING)
    private FuelType fuelType;        // Type de carburant

    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;  // Type de transmission

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicle_additional_photos", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "photo_url")
    private List<String> additionalPhotos = new ArrayList<>(); // Initialize as an empty list


}
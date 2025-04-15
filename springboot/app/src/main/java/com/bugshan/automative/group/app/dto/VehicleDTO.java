package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {

    private Long id;
    private String imageUrl;
    private String make;            // Marque du véhicule
    private String model;           // Modèle du véhicule
    private int year;               // Année de fabrication
    private String color;           // Couleur du véhicule
    private String vin;             // Numéro de série (VIN)
    private int mileage;            // Kilométrage
    private int seats;              // Nombre de sièges
    private VehicleType vehicleType; // Type de véhicule
    private double retailPrice;     // Prix par jour
    private boolean available;      // Disponibilité du véhicule
    private String location;        // Emplacement actuel
    private boolean insured;        // Si le véhicule est assuré
    private VehicleCondition vehicleCondition; // Condition du véhicule
    private double fuelLevel;       // Niveau de carburant
    private FuelType fuelType;      // Type de carburant
    private TransmissionType transmissionType; // Type de transmission
    private List<VehiclePhotoDTO> additionalPhotos; // List of additional photo DTOs

    // Constructeur personnalisé pour transformer une entité Vehicle en DTO
    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.imageUrl = vehicle.getImageUrl();
        this.make = vehicle.getMake();
        this.model = vehicle.getModel();
        this.year = vehicle.getYear();
        this.color = vehicle.getColor();
        this.vin = vehicle.getVin();
        this.mileage = vehicle.getMileage();
        this.seats = vehicle.getSeats();
        this.vehicleType = vehicle.getVehicleType();
        this.retailPrice = vehicle.getRetailPrice();
        this.available = vehicle.isAvailable();
        this.location = vehicle.getLocation();
        this.insured = vehicle.isInsured();
        this.vehicleCondition = vehicle.getVehicleCondition();
        this.fuelLevel = vehicle.getFuelLevel();
        this.fuelType = vehicle.getFuelType();
        this.transmissionType = vehicle.getTransmissionType();

        // Convert additional photos to a list of VehiclePhotoDTO
        this.additionalPhotos = vehicle.getAdditionalPhotos().stream()
                .map(VehiclePhotoDTO::new)  // Using the VehiclePhotoDTO constructor
                .collect(Collectors.toList());
    }
}

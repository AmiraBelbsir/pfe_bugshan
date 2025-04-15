package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.VehiclePhoto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VehiclePhotoDTO {

    private Long id;            // ID de la photo
    private Long vehicleId;     // ID du véhicule associé à la photo
    private String photoUrl;    // URL ou chemin de la photo


    public VehiclePhotoDTO(VehiclePhoto vehiclePhoto) {
        this.id = vehiclePhoto.getId();
        this.vehicleId = vehiclePhoto.getVehicle() != null ? vehiclePhoto.getVehicle().getId() : null;
        this.photoUrl = vehiclePhoto.getPhotoUrl();
    }


    public VehiclePhotoDTO(String photoUrl) {
    }
}

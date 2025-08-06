package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.PhotoVehicule;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PhotoVehiculeDTO {

    private Long id;               // ID de la photo
    private Long vehiculeId;       // ID du véhicule associé
    private String urlPhoto;       // URL ou chemin de la photo

    public PhotoVehiculeDTO(PhotoVehicule photoVehicule) {
        this.id = photoVehicule.getId();
        this.vehiculeId = photoVehicule.getVehicule() != null ? photoVehicule.getVehicule().getId() : null;
        this.urlPhoto = photoVehicule.getUrlPhoto();
    }

    public PhotoVehiculeDTO(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}

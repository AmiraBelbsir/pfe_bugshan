package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.VehiculeDTO;
import com.bugshan.automative.group.app.model.*;
import com.bugshan.automative.group.app.repository.VehiculeRepository;
import com.bugshan.automative.group.app.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicules")
@CrossOrigin(origins = "http://localhost:4200")
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    // Récupérer tous les véhicules
    @GetMapping
    public List<VehiculeDTO> getVehicules() {
        return vehiculeService.trouverTousLesVehicules().stream()
                .map(VehiculeDTO::new)
                .collect(Collectors.toList());
    }

    // Récupérer un véhicule par ID avec DTO
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> getVehiculeParId(@PathVariable Long id) {
        Optional<Vehicule> vehicule = vehiculeService.trouverVehiculeParId(id);

        return vehicule
                .map(v -> ResponseEntity.ok(new VehiculeDTO(v))) // transformer en DTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Créer un nouveau véhicule avec image(s) optionnelle(s)
    @PostMapping
    public ResponseEntity<?> creerVehiculeAvecImage(
            @RequestParam(value = "file", required = false) MultipartFile fichierPrincipal,
            @RequestParam(value = "additionalFiles", required = false) List<MultipartFile> fichiersAdditionnels,
            @RequestParam("marque") String marque,
            @RequestParam("modele") String modele,
            @RequestParam("quantite") int quantite,
            @RequestParam("annee") int annee,
            @RequestParam("couleur") String couleur,
            @RequestParam("sieges") int sieges,
            @RequestParam("typeVehicule") String typeVehicule,
            @RequestParam("prix") double prix,
            @RequestParam("emplacement") String emplacement,

            @RequestParam("niveauCarburant") double niveauCarburant,
            @RequestParam("typeCarburant") String typeCarburant,
            @RequestParam("typeTransmission") String typeTransmission) {

        Vehicule vehicule = new Vehicule();
        vehicule.setMarque(marque);
        vehicule.setModele(modele);
        vehicule.setQuantite(quantite);
        vehicule.setAnnee(annee);
        vehicule.setCouleur(couleur);
        vehicule.setSieges(sieges);
        vehicule.setTypeVehicule(TypeVehicule.valueOf(typeVehicule.toUpperCase()));
        vehicule.setPrix(prix);
        vehicule.setEmplacement(emplacement);
        vehicule.setNiveauCarburant(niveauCarburant);
        vehicule.setTypeCarburant(TypeCarburant.valueOf(typeCarburant.toUpperCase()));
        vehicule.setTypeTransmission(TypeTransmission.valueOf(typeTransmission.toUpperCase()));

        try {
            // Upload image principale
            if (fichierPrincipal != null && !fichierPrincipal.isEmpty()) {
                String nomFichier = UUID.randomUUID() + "_" + fichierPrincipal.getOriginalFilename();
                Path cheminFichier = Paths.get("uploads", nomFichier);
                Files.createDirectories(cheminFichier.getParent());
                Files.write(cheminFichier, fichierPrincipal.getBytes());
                vehicule.setUrlImage(nomFichier);
            }

            // Upload images additionnelles
            if (fichiersAdditionnels != null && !fichiersAdditionnels.isEmpty()) {
                for (MultipartFile fichierAdditionnel : fichiersAdditionnels) {
                    String nomFichierAdd = UUID.randomUUID() + "_" + fichierAdditionnel.getOriginalFilename();
                    Path cheminFichierAdd = Paths.get("uploads", nomFichierAdd);
                    Files.createDirectories(cheminFichierAdd.getParent());
                    Files.write(cheminFichierAdd, fichierAdditionnel.getBytes());

                    PhotoVehicule photo = new PhotoVehicule();
                    photo.setUrlPhoto(nomFichierAdd);
                    photo.setVehicule(vehicule);
                    vehicule.getPhotosAdditionnelles().add(photo.getUrlPhoto());
                }
            }

            Vehicule vehiculeSauvegarde = vehiculeService.ajouterVehicule(vehicule);
            return ResponseEntity.ok(new VehiculeDTO(vehiculeSauvegarde));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec du téléchargement des images");
        }
    }

    // Mettre à jour un véhicule
    @PutMapping("/{id}")
    public ResponseEntity<?> mettreAJourVehicule(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile fichierPrincipal,
            @RequestParam(value = "additionalFiles", required = false) List<MultipartFile> fichiersAdditionnels,
            @RequestParam(value = "marque", required = false) String marque,
            @RequestParam(value = "modele", required = false) String modele,
            @RequestParam(value = "quantite", required = false) Integer quantite,
            @RequestParam(value = "annee", required = false) Integer annee,
            @RequestParam(value = "couleur", required = false) String couleur,
            @RequestParam(value = "sieges", required = false) Integer sieges,
            @RequestParam(value = "typeVehicule", required = false) String typeVehicule,
            @RequestParam(value = "prix", required = false) Double prix,
            @RequestParam(value = "emplacement", required = false) String emplacement,
            @RequestParam(value = "niveauCarburant", required = false) Double niveauCarburant,
            @RequestParam(value = "typeCarburant", required = false) String typeCarburant,
            @RequestParam(value = "typeTransmission", required = false) String typeTransmission) {

        Optional<Vehicule> vehiculeExistantOpt = vehiculeRepository.findById(id);
        if (!vehiculeExistantOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Véhicule non trouvé");
        }

        Vehicule vehiculeExistant = vehiculeExistantOpt.get();

        if (marque != null) vehiculeExistant.setMarque(marque);
        if (modele != null) vehiculeExistant.setModele(modele);
        if (quantite != null) vehiculeExistant.setQuantite(quantite);
        if (annee != null) vehiculeExistant.setAnnee(annee);
        if (couleur != null) vehiculeExistant.setCouleur(couleur);
        if (sieges != null) vehiculeExistant.setSieges(sieges);
        if (typeVehicule != null) vehiculeExistant.setTypeVehicule(TypeVehicule.valueOf(typeVehicule.toUpperCase()));
        if (prix != null) vehiculeExistant.setPrix(prix);
        if (emplacement != null) vehiculeExistant.setEmplacement(emplacement);
        if (niveauCarburant != null) vehiculeExistant.setNiveauCarburant(niveauCarburant);
        if (typeCarburant != null) vehiculeExistant.setTypeCarburant(TypeCarburant.valueOf(typeCarburant.toUpperCase()));
        if (typeTransmission != null) vehiculeExistant.setTypeTransmission(TypeTransmission.valueOf(typeTransmission.toUpperCase()));

        try {
            if (fichierPrincipal != null && !fichierPrincipal.isEmpty()) {
                String nomFichier = UUID.randomUUID() + "_" + fichierPrincipal.getOriginalFilename();
                Path cheminFichier = Paths.get("uploads", nomFichier);
                Files.createDirectories(cheminFichier.getParent());
                Files.write(cheminFichier, fichierPrincipal.getBytes());
                vehiculeExistant.setUrlImage(nomFichier);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec du téléchargement de l'image principale");
        }

        if (fichiersAdditionnels != null && !fichiersAdditionnels.isEmpty()) {
            for (MultipartFile fichierAdditionnel : fichiersAdditionnels) {
                try {
                    String nomFichierAdd = UUID.randomUUID() + "_" + fichierAdditionnel.getOriginalFilename();
                    Path cheminFichierAdd = Paths.get("uploads", nomFichierAdd);
                    Files.createDirectories(cheminFichierAdd.getParent());
                    Files.write(cheminFichierAdd, fichierAdditionnel.getBytes());

                    PhotoVehicule photo = new PhotoVehicule();
                    photo.setUrlPhoto(nomFichierAdd);
                    photo.setVehicule(vehiculeExistant);
                    vehiculeExistant.getPhotosAdditionnelles().add(photo.getUrlPhoto());
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec du téléchargement d'une photo additionnelle");
                }
            }
        }

        vehiculeRepository.save(vehiculeExistant);
        return ResponseEntity.ok(vehiculeExistant);
    }

    // Supprimer un véhicule par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVehicule(@PathVariable Long id) {
        if (!vehiculeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        vehiculeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

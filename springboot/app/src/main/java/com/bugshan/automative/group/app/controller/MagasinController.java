package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.MagasinDTO;
import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.model.Magasin;
import com.bugshan.automative.group.app.repository.BlocRepository;
import com.bugshan.automative.group.app.service.MagasinService;
import com.bugshan.automative.group.app.repository.MagasinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/magasins")
@CrossOrigin(origins = "http://localhost:4200")// Autoriser l'accès depuis Angular
public class MagasinController {

    @Autowired
    private MagasinService magasinService;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private MagasinRepository magasinRepository;



    @GetMapping
    public List<MagasinDTO> getAllMagasins() {
        return magasinService.getAllMagasins();
    }
    // Récupérer tous les magasins actifs (entités)
    @GetMapping("/actifs")
    public List<Magasin> getAllMagasinsActifs() {
        return magasinService.findAllMagasinsActifs();
    }

    // Récupérer un magasin par ID
    @GetMapping("/{id}")
    public ResponseEntity<MagasinDTO> getMagasinById(@PathVariable Long id) {
        return magasinService.findMagasinById(id)
                .map(magasin -> ResponseEntity.ok(new MagasinDTO(magasin)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createMagasin(
            @RequestParam("nom") String nom,
            @RequestParam("adresse") String adresse
    ) {
        // Vérifier si un magasin avec le même nom existe déjà (optionnel)
        if (magasinService.existsByNom(nom)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Un magasin avec ce nom existe déjà."));
        }

        Magasin magasin = Magasin.builder()
                .nom(nom)
                .adresse(adresse)
                .actif(true) // actif par défaut
                .build();

        Magasin saved = magasinService.saveMagasin(magasin);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateMagasin(
            @PathVariable Long id,
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "actif", required = false) Boolean actif
    ) {
        Optional<Magasin> existingMagasinOpt = magasinService.findMagasinById(id);

        if (existingMagasinOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Magasin non trouvé.");
        }

        Magasin existingMagasin = existingMagasinOpt.get();

        // Vérification de nom en double
        if (nom != null && !nom.equals(existingMagasin.getNom()) && magasinService.existsByNom(nom)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("field", "nom", "message", "Ce nom de magasin est déjà utilisé."));
        }

        // Mise à jour des champs
        if (nom != null) existingMagasin.setNom(nom);
        if (adresse != null) existingMagasin.setAdresse(adresse);
        if (actif != null) existingMagasin.setActif(actif);

        Magasin updated = magasinService.saveMagasin(existingMagasin);

        return ResponseEntity.ok(new MagasinDTO(updated));
    }


    // Activer / désactiver un magasin
    @PutMapping("/{id}/active")
    public ResponseEntity<MagasinDTO> setActive(@PathVariable Long id, @RequestParam boolean active) {
        return magasinService.setActive(id, active)
                .map(magasin -> ResponseEntity.ok(new MagasinDTO(magasin)))
                .orElse(ResponseEntity.notFound().build());
    }
    // 🔵 Ajouter un bloc à un magasin
    @PostMapping("/{magasinId}/blocs")
    public ResponseEntity<?> addBlocToMagasin(
            @PathVariable Long magasinId,
            @RequestParam String nom,
            @RequestParam String description
    ) {
        try {
            Bloc bloc = magasinService.createBlocForMagasin(magasinId, nom, description);
            return ResponseEntity.status(HttpStatus.CREATED).body(bloc);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

}




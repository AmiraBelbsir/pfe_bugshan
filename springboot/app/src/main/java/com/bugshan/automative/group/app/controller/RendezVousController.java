package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.RendezVousDTO;
import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.model.RendezVous.StatutRdv;
import com.bugshan.automative.group.app.service.RendezVousService;
import com.bugshan.automative.group.app.service.UtilisateurService;
import com.bugshan.automative.group.app.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rendezvous")
@CrossOrigin(origins = "http://localhost:4200")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    // Optionnel : si tu as des services pour user/vehicule afin d'assurer existence
    @Autowired
    private UtilisateurService userService;

    @Autowired
    private VehiculeService vehiculeService;

    // Récupérer tous les rendez-vous (DTO)
    @GetMapping
    public List<RendezVousDTO> getAll() {
        return rendezVousService.getAll().stream()
                .map(RendezVousDTO::new)
                .collect(Collectors.toList());
    }

    // Récupérer par ID
    @GetMapping("/{id}")
    public ResponseEntity<RendezVousDTO> getById(@PathVariable Long id) {
        return rendezVousService.getById(id)
                .map(rdv -> ResponseEntity.ok(new RendezVousDTO(rdv)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un rendez-vous
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RendezVous rdv) {
        // Défaut statut si non fourni
        if (rdv.getStatut() == null) {
            rdv.setStatut(StatutRdv.EN_ATTENTE);
        }

        // Ici tu pourrais valider l'existence du client/commercial/véhicule si tu changes les relations
        // ex: if (rdv.getCommercialId() != null && userService.findUserById(rdv.getCommercialId()).isEmpty()) ...

        RendezVous saved = rendezVousService.save(rdv);
        return ResponseEntity.status(201).body(new RendezVousDTO(saved));
    }



    // Mettre à jour
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RendezVous rdvDetails) {
        try {
            RendezVous updated = rendezVousService.update(id, rdvDetails);
            return ResponseEntity.ok(new RendezVousDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rendezVousService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

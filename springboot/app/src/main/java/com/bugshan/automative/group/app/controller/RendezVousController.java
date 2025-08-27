package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.RendezVousDTO;
import com.bugshan.automative.group.app.model.Magasin;
import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.model.RendezVous.StatutRdv;
import com.bugshan.automative.group.app.model.Utilisateur;
import com.bugshan.automative.group.app.model.Vehicule;
import com.bugshan.automative.group.app.repository.MagasinRepository;
import com.bugshan.automative.group.app.repository.RendezVousRepository;
import com.bugshan.automative.group.app.repository.UtilisateurRepository;
import com.bugshan.automative.group.app.repository.VehiculeRepository;
import com.bugshan.automative.group.app.service.RendezVousService;
import com.bugshan.automative.group.app.service.UtilisateurService;
import com.bugshan.automative.group.app.service.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private MagasinRepository magasinRepository;
    @Autowired
    private VehiculeRepository vehiculeRepository;

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


    @PostMapping("/add")
    public ResponseEntity<?> creerRendezVous(
            @RequestParam("date") String dateStr,
            @RequestParam("heure") String heureStr,
            @RequestParam("clientId") Long clientId,
            @RequestParam("magasinId") Long magasinId,
            @RequestParam("vehiculeId") Long vehiculeId) {

        try {
            // Convertir en LocalDate et LocalTime
            LocalDate date = LocalDate.parse(dateStr);
            LocalTime heure = LocalTime.parse(heureStr);

            // Vérifier existence client
            Utilisateur client = utilisateurRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client introuvable avec ID: " + clientId));

            // Vérifier existence magasin
            Magasin magasin = magasinRepository.findById(magasinId)
                    .orElseThrow(() -> new RuntimeException("Magasin introuvable avec ID: " + magasinId));

            // Vérifier existence véhicule
            Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                    .orElseThrow(() -> new RuntimeException("Véhicule introuvable avec ID: " + vehiculeId));

            // Vérifier s'il existe déjà un RDV actif (EN_ATTENTE ou VALIDE)
            boolean rdvExist = rendezVousRepository.existsByClientAndVehiculeAndStatutIn(
                    client, vehicule, List.of(RendezVous.StatutRdv.EN_ATTENTE, RendezVous.StatutRdv.VALIDE));

            if (rdvExist) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Vous avez déjà un rendez-vous pour ce véhicule en attente ou validé."));
            }

            // Construire le rendez-vous
            RendezVous rdv = RendezVous.builder()
                    .date(date)
                    .heure(heure)
                    .client(client)
                    .magasin(magasin)
                    .vehicule(vehicule)
                    .statut(RendezVous.StatutRdv.EN_ATTENTE) // statut par défaut
                    .build();

            // Sauvegarder
            RendezVous savedRdv = rendezVousRepository.save(rdv);

            // Convertir en DTO (ton constructeur fait le boulot 👌)
            RendezVousDTO dto = new RendezVousDTO(savedRdv);

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Erreur lors de la création du rendez-vous", "erreur", e.getMessage()));
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRendezVous(
            @PathVariable("id") Long id,
            @RequestParam(value = "date", required = false) String dateStr,
            @RequestParam(value = "heure", required = false) String heureStr,
            @RequestParam(value = "clientId", required = false) Long clientId,
            @RequestParam(value = "magasinId", required = false) Long magasinId,
            @RequestParam(value = "vehiculeId", required = false) Long vehiculeId,
            @RequestParam(value = "statut", required = false) String statutStr) {

        try {
            // Vérifier si le rendez-vous existe
            RendezVous rdv = rendezVousRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable avec ID: " + id));

            // Mise à jour des champs uniquement si fournis
            if (dateStr != null) {
                rdv.setDate(LocalDate.parse(dateStr));
            }

            if (heureStr != null) {
                rdv.setHeure(LocalTime.parse(heureStr));
            }

            if (clientId != null) {
                Utilisateur client = utilisateurRepository.findById(clientId)
                        .orElseThrow(() -> new RuntimeException("Client introuvable avec ID: " + clientId));
                rdv.setClient(client);
            }

            if (magasinId != null) {
                Magasin magasin = magasinRepository.findById(magasinId)
                        .orElseThrow(() -> new RuntimeException("Magasin introuvable avec ID: " + magasinId));
                rdv.setMagasin(magasin);
            }

            if (vehiculeId != null) {
                Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                        .orElseThrow(() -> new RuntimeException("Véhicule introuvable avec ID: " + vehiculeId));
                rdv.setVehicule(vehicule);
            }

            if (statutStr != null) {
                try {
                    RendezVous.StatutRdv statut = RendezVous.StatutRdv.valueOf(statutStr.toUpperCase());
                    rdv.setStatut(statut);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Statut invalide: " + statutStr);
                }
            }

            // Sauvegarder les modifications
            RendezVous updatedRdv = rendezVousRepository.save(rdv);

            return ResponseEntity.ok(updatedRdv);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Erreur lors de la mise à jour du rendez-vous", "erreur", e.getMessage()));
        }
    }




    // Supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rendezVousService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer tous les rendez-vous d'un client par son ID
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RendezVousDTO>> getByClientId(@PathVariable Long clientId) {
        List<RendezVous> rdvs = rendezVousRepository.findByClientId(clientId);

        if (rdvs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<RendezVousDTO> rdvDTOs = rdvs.stream()
                .map(RendezVousDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rdvDTOs);
    }


    @PutMapping("/cancel/{id}")
    public ResponseEntity<RendezVous> cancelRdv(@PathVariable Long id) {
        Optional<RendezVous> rdvOpt = rendezVousRepository.findById(id);

        if (rdvOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RendezVous rdv = rdvOpt.get();
        rdv.setStatut(StatutRdv.REFUSE);
        RendezVous updated = rendezVousRepository.save(rdv);

        return ResponseEntity.ok(updated);
    }


}







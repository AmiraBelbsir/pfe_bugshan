package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.RendezVousDTO;
import com.bugshan.automative.group.app.model.Magasin;
import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.model.Utilisateur;
import com.bugshan.automative.group.app.repository.MagasinRepository;
import com.bugshan.automative.group.app.repository.RendezVousRepository;
import com.bugshan.automative.group.app.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private MagasinRepository magasinRepository;


    public List<RendezVous> getAll() {
        return rendezVousRepository.findAll();
    }

    public Optional<RendezVous> getById(Long id) {
        return rendezVousRepository.findById(id);
    }

    public RendezVous save(RendezVous rdv) {
        return rendezVousRepository.save(rdv);
    }

    public RendezVous update(Long id, RendezVous rdvDetails) {
        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé avec l'id: " + id));

        // Mise à jour du commercial s'il est fourni
        if (rdvDetails.getCommercial() != null && rdvDetails.getCommercial().getId() != null) {
            Utilisateur commercial = utilisateurRepository.findById(rdvDetails.getCommercial().getId())
                    .orElseThrow(() -> new RuntimeException("Commercial non trouvé avec l'id: " + rdvDetails.getCommercial().getId()));
            rdv.setCommercial(commercial);
        }

        // Même logique peut être appliquée au client ou au véhicule si tu veux
        if (rdvDetails.getVehicule() != null && rdvDetails.getVehicule().getId() != null) {
            rdv.setVehicule(rdvDetails.getVehicule());  // ou charger le véhicule aussi par ID si nécessaire
        }

        // Statut
        if (rdvDetails.getStatut() != null) {
            rdv.setStatut(rdvDetails.getStatut());
        }

        // Date et heure (si elles doivent être modifiables)
        if (rdvDetails.getDate() != null) {
            rdv.setDate(rdvDetails.getDate());
        }
        if (rdvDetails.getHeure() != null) {
            rdv.setHeure(rdvDetails.getHeure());
        }

// Magasin
        if (rdvDetails.getMagasin() != null && rdvDetails.getMagasin().getId() != null) {
            Magasin magasin = magasinRepository.findById(rdvDetails.getMagasin().getId())
                    .orElseThrow(() -> new RuntimeException("Magasin non trouvé avec l'id: " + rdvDetails.getMagasin().getId()));
            rdv.setMagasin(magasin);
        }


        return rendezVousRepository.save(rdv);
    }



    public void delete(Long id) {
        rendezVousRepository.deleteById(id);
    }


}
package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

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
        Optional<RendezVous> optionalRdv = rendezVousRepository.findById(id);
        if (optionalRdv.isPresent()) {
            RendezVous rdv = optionalRdv.get();

            rdv.setDate(rdvDetails.getDate());
            rdv.setHeure(rdvDetails.getHeure());
            rdv.setClientNom(rdvDetails.getClientNom());
            rdv.setVehiculeNom(rdvDetails.getVehiculeNom());
            rdv.setCommercialId(rdvDetails.getCommercialId());
            rdv.setCommercialNom(rdvDetails.getCommercialNom());
            rdv.setStatut(rdvDetails.getStatut());

            return rendezVousRepository.save(rdv);
        } else {
            throw new RuntimeException("Rendez-vous non trouvé avec l'id: " + id);
        }
    }

    public void delete(Long id) {
        rendezVousRepository.deleteById(id);
    }
}

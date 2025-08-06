package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.VehiculeDTO;
import com.bugshan.automative.group.app.model.Vehicule;
import com.bugshan.automative.group.app.repository.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    public VehiculeService(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    // Ajouter un véhicule
    public Vehicule ajouterVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    // Rechercher un véhicule par son ID
    public Optional<Vehicule> trouverVehiculeParId(Long id) {
        return vehiculeRepository.findById(id);
    }

    // Récupérer tous les véhicules
    public List<Vehicule> trouverTousLesVehicules() {
        return vehiculeRepository.findAll();
    }
}

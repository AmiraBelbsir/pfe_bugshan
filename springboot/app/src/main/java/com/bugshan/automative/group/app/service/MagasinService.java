package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.MagasinDTO;
import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.model.Magasin;
import com.bugshan.automative.group.app.repository.BlocRepository;
import com.bugshan.automative.group.app.repository.MagasinRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MagasinService {


    @Autowired
    private BlocRepository blocRepository;
    @Autowired
    private MagasinRepository magasinRepository;
    public List<Magasin> getAllActifs() {
        return magasinRepository.findByActifTrue();
    }
    public List<MagasinDTO> getAllMagasins() {
        List<Magasin> magasins = magasinRepository.findAll();

        // ⚠️ Conversion manuelle vers DTO
        return magasins.stream()
                .map(magasin -> {
                    MagasinDTO dto = new MagasinDTO();
                    dto.setId(magasin.getId());
                    dto.setNom(magasin.getNom());
                    dto.setAdresse(magasin.getAdresse());
                    dto.setActif(magasin.isActif());
                    // ➕ Ajout des noms des blocs associés
                    if (magasin.getBlocs() != null) {
                        List<String> nomsBlocs = magasin.getBlocs().stream()
                                .map(bloc -> bloc.getNom())
                                .collect(Collectors.toList());
                        dto.setNomsBlocs(nomsBlocs);
                    }
                    // Ajoute ici d'autres champs si nécessaire
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Autowired
    public MagasinService(MagasinRepository magasinRepository) {
        this.magasinRepository = magasinRepository;
    }

    // Ajouter un magasin
    public Magasin addMagasin(MagasinDTO magasinRequest) {
        Magasin magasin = new Magasin();

        magasin.setNom(magasinRequest.getNom());
        magasin.setAdresse(magasinRequest.getAdresse());


        return magasinRepository.save(magasin);
    }
    // Trouver un magasin par ID
    public Optional<Magasin> findMagasinById(Long id) {
        return magasinRepository.findById(id);
    }

    // Trouver un magasin par nom exact
    public Optional<Magasin> findMagasinByNom(String nom) {
        return magasinRepository.findByNom(nom);
    }

    // Vérifier si un nom de magasin existe
    public boolean existsByNom(String nom) {
        return magasinRepository.existsByNom(nom);
    }
    public Magasin saveMagasin(Magasin magasin) {
        return magasinRepository.save(magasin);
    }

    // Récupérer tous les magasins (entités)
    public List<Magasin> findAllMagasins() {
        return magasinRepository.findAll();
    }

    // Récupérer tous les magasins actifs
    public List<Magasin> findAllMagasinsActifs() {
        return magasinRepository.findByActifTrue();
    }
    // Modifier un magasin
    public Optional<Magasin> updateMagasin(Long id, Magasin magasinDetails) {
        return magasinRepository.findById(id).map(magasin -> {
            if (magasinDetails.getNom() != null) magasin.setNom(magasinDetails.getNom());
            if (magasinDetails.getAdresse() != null) magasin.setAdresse(magasinDetails.getAdresse());
            return magasinRepository.save(magasin);
        });
    }

    // Activer ou désactiver un magasin
    public Optional<Magasin> setActive(Long id, boolean active) {
        return magasinRepository.findById(id).map(magasin -> {
            magasin.setActif(active);
            return magasinRepository.save(magasin);
        });
    }
    public Bloc createBlocForMagasin(Long magasinId, String nom, String description) {
        Magasin magasin = magasinRepository.findById(magasinId)
                .orElseThrow(() -> new RuntimeException("Magasin non trouvé"));

        Bloc bloc = Bloc.builder()
                .nom(nom)
                .description(description)
                .magasin(magasin)
                .build();

        return blocRepository.save(bloc);

    }

}




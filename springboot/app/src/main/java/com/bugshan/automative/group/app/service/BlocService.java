package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.BlocDTO;
import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.repository.BlocRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlocService {

    @Autowired
    private BlocRepository blocRepository;

    public List<BlocDTO> getAllBlocs() {
        List<Bloc> blocs = blocRepository.findAll();

        // ⚠️ Conversion manuelle vers BlocDTO
        return blocs.stream()
                .map(bloc -> {
                    BlocDTO dto = new BlocDTO();
                    dto.setId(bloc.getId());
                    dto.setNom(bloc.getNom());
                    dto.setMagasinId(bloc.getMagasin().getId()); // relation vers Magasin
                    return dto;
                })
                .collect(Collectors.toList());
    }

    //  Méthode pour obtenir un bloc par ID
    public BlocDTO getBlocById(Long id) {
        Bloc bloc = blocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloc non trouvé avec l'ID : " + id));

        BlocDTO dto = new BlocDTO();
        dto.setId(bloc.getId());
        dto.setNom(bloc.getNom());
        dto.setMagasinId(bloc.getMagasin().getId());
        return dto;
    }
    // Méthode qui récupère les blocs par magasinId
    public List<BlocDTO> getBlocsByMagasinId(Long magasinId) {
        List<Bloc> blocs = blocRepository.findByMagasinId(magasinId);
        return blocs.stream()
                .map(BlocDTO::new)
                .collect(Collectors.toList());
    }
}

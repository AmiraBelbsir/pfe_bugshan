package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.AvisDTO;
import com.bugshan.automative.group.app.dto.BlocDTO;
import com.bugshan.automative.group.app.model.Avis;
import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.repository.AvisRepository;
import com.bugshan.automative.group.app.repository.BlocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvisService {

    @Autowired
    private AvisRepository avisRepository;

    public List<AvisDTO> getAllAvis() {
        List<Avis> avisList = avisRepository.findAll();

        return avisList.stream()
                .map(avis -> {
                    AvisDTO dto = new AvisDTO();
                    dto.setId(avis.getId());
                    dto.setCommentaire(avis.getCommentaire());
                    dto.setNote(avis.getNote());
                    dto.setDateCreation(avis.getDateCreation());
                    dto.setUtilisateurId(
                            avis.getUtilisateur() != null ? avis.getUtilisateur().getId() : null
                    );
                    dto.setUtilisateurNom(avis.getUtilisateur().getNomComplet());
                    dto.setVehiculeMarque(avis.getVehicule().getMarque());
                    dto.setVehiculeModele(avis.getVehicule().getModele());

                    dto.setVehiculeId(
                            avis.getVehicule().getId()
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }


}

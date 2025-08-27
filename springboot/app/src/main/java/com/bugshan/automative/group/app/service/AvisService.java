package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.AvisDTO;
import com.bugshan.automative.group.app.dto.BlocDTO;
import com.bugshan.automative.group.app.model.Avis;
import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.model.RendezVous;
import com.bugshan.automative.group.app.model.Utilisateur;
import com.bugshan.automative.group.app.repository.AvisRepository;
import com.bugshan.automative.group.app.repository.BlocRepository;
import com.bugshan.automative.group.app.repository.RendezVousRepository;
import com.bugshan.automative.group.app.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvisService {

    @Autowired
    private AvisRepository avisRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private RendezVousRepository rdvRepository;

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


    public AvisDTO createAvis(Long rdvId, int note, String commentaire, Long utilisateurId) {
        RendezVous rdv = rdvRepository.findById(rdvId)
                .orElseThrow(() -> new RuntimeException("RDV introuvable"));

        if (rdv.getStatut() != RendezVous.StatutRdv.TERMINE) {
            throw new RuntimeException("Impossible de soumettre un avis sur un RDV non terminé");
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Avis avis = Avis.builder()
                .rdv(rdv)
                .vehicule(rdv.getVehicule())
                .utilisateur(utilisateur)
                .note(note)
                .commentaire(commentaire)
                .dateCreation(LocalDateTime.now())
                .build();

        Avis savedAvis = avisRepository.save(avis);
        return new AvisDTO(savedAvis);
    }

}

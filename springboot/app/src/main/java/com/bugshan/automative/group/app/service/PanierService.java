package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.PanierDTO;
import com.bugshan.automative.group.app.model.Panier;
import com.bugshan.automative.group.app.model.Utilisateur;
import com.bugshan.automative.group.app.repository.PanierRepository;
import com.bugshan.automative.group.app.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PanierService {

    private final PanierRepository panierRepository;
    private final UtilisateurRepository utilisateurRepository;

    public PanierDTO getOrCreatePanier(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Optional<Panier> existingPanier = panierRepository.findByUtilisateurIdAndConfirmeFalse(utilisateurId);

        Panier panier = existingPanier.orElseGet(() -> {
            Panier newPanier = new Panier();
            newPanier.setUtilisateur(utilisateur);
            return panierRepository.save(newPanier);
        });

        return new PanierDTO(panier);
    }

    public PanierDTO confirmerPanier(Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));
        panier.setConfirme(true);
        return new PanierDTO(panierRepository.save(panier));
    }
}


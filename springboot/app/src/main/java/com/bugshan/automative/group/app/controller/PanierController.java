package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.LignePanierDTO;
import com.bugshan.automative.group.app.dto.PanierDTO;
import com.bugshan.automative.group.app.model.*;
import com.bugshan.automative.group.app.repository.*;
import com.bugshan.automative.group.app.service.PanierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/paniers")
@RequiredArgsConstructor
public class PanierController {

    private final PanierRepository panierRepository;
    private final CommandeRepository commandeRepository;

    private final LignePanierRepository lignePanierRepository;
    private final PieceRepository pieceRepository;
    private final UtilisateurRepository utilisateurRepository;
    @PostMapping("/add")
    public ResponseEntity<PanierDTO> addPieceToPanier(@RequestBody PanierDTO panierDTO) {
        Panier panier = panierRepository
                .findByUtilisateurIdAndConfirmeFalse(panierDTO.getUtilisateurId())
                .orElseGet(() -> {
                    Utilisateur utilisateur = utilisateurRepository.findById(panierDTO.getUtilisateurId())
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Panier newPanier = new Panier();
                    newPanier.setUtilisateur(utilisateur);
                    newPanier.setConfirme(false);
                    return panierRepository.save(newPanier);
                });

        LignePanierDTO ligneDTO = panierDTO.getLigne();
        Piece piece = pieceRepository.findById(ligneDTO.getPieceId())
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée"));

        LignePanier ligne = LignePanier.builder()
                .piece(piece)
                .quantite(ligneDTO.getQuantite())
                .livraison(ligneDTO.getLivraison())
                .panier(panier)
                .build();

        panier.getLignes().add(ligne);
        panierRepository.save(panier);

        return ResponseEntity.ok(PanierDTO.fromEntity(panier));
    }

    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<PanierDTO> getPanierByUtilisateur(@PathVariable Long utilisateurId) {
        Panier panier = panierRepository.findByUtilisateurIdAndConfirmeFalse(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Aucun panier en cours trouvé pour cet utilisateur"));

        return ResponseEntity.ok(new PanierDTO(panier));
    }


    @PutMapping("/{panierId}/ligne/{ligneId}")
    public ResponseEntity<LignePanierDTO> updateLignePanier(
            @PathVariable Long panierId,
            @PathVariable Long ligneId,
            @RequestBody LignePanierDTO ligneDTO) {

        LignePanier ligne = lignePanierRepository.findById(ligneId)
                .orElseThrow(() -> new RuntimeException("Ligne non trouvée"));

        ligne.setQuantite(ligneDTO.getQuantite());
        ligne.setLivraison(ligneDTO.getLivraison());

        LignePanier updated = lignePanierRepository.save(ligne);

        return ResponseEntity.ok(LignePanierDTO.fromEntity(updated));
    }

    @DeleteMapping("/{panierId}/ligne/{ligneId}")
    public ResponseEntity<PanierDTO> removeLigneFromPanier(
            @PathVariable Long panierId,
            @PathVariable Long ligneId) {

        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        LignePanier ligne = lignePanierRepository.findById(ligneId)
                .orElseThrow(() -> new RuntimeException("Ligne non trouvée"));

        panier.getLignes().remove(ligne);
        lignePanierRepository.delete(ligne);

        panierRepository.save(panier); // persist changes

        return ResponseEntity.ok(PanierDTO.fromEntity(panier));
    }


    @PutMapping("/{panierId}/confirm")
    public ResponseEntity<Commande> confirmPanier(@PathVariable Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.isConfirme()) {
            throw new RuntimeException("Ce panier est déjà confirmé");
        }

        panier.setConfirme(true);
        panierRepository.save(panier);

        // Transformer les lignes du panier en lignes de commande
        List<LigneCommande> lignesCommande = panier.getLignes().stream()
                .map(lp -> LigneCommande.builder()
                        .piece(lp.getPiece())
                        .quantite(lp.getQuantite())
                        .livraison(lp.getLivraison())
                        .build()
                ).toList();

        // Calcul du total
        double totalCommande = lignesCommande.stream()
                .mapToDouble(lc -> lc.getPrixUnitaire() * lc.getQuantite()
                        + ("express".equalsIgnoreCase(lc.getLivraison()) ? 120 : 50))
                .sum();

        // Créer la commande
        Commande commande = Commande.builder()
                .utilisateur(panier.getUtilisateur())
                .lignes(lignesCommande)
                .total(totalCommande)      // <-- assignation ici
                .statut("EN_ATTENTE")
                .dateCommande(LocalDateTime.now())
                .build();

        Commande savedCommande = commandeRepository.save(commande);

        return ResponseEntity.ok(savedCommande);
    }

}

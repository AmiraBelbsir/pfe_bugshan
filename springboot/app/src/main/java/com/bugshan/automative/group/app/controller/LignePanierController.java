package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.LignePanierDTO;
import com.bugshan.automative.group.app.service.LignePanierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lignes-panier")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")// Autoriser l'accès depuis Angular
public class LignePanierController {

    private final LignePanierService lignePanierService;

    // Ajouter une pièce au panier
    @PostMapping("/ajouter")
    public LignePanierDTO ajouterLigne(
            @RequestParam Long panierId,
            @RequestParam Long pieceId,
            @RequestParam int quantite,
            @RequestParam(defaultValue = "standard") String livraison
    ) {
        return lignePanierService.ajouterLigne(panierId, pieceId, quantite, livraison);
    }

    // Supprimer une ligne du panier
    @DeleteMapping("/{ligneId}")
    public void supprimerLigne(@PathVariable Long ligneId) {
        lignePanierService.supprimerLigne(ligneId);
    }
}

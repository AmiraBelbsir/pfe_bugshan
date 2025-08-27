package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.LignePanierDTO;
import com.bugshan.automative.group.app.model.LignePanier;
import com.bugshan.automative.group.app.model.Panier;
import com.bugshan.automative.group.app.model.Piece;
import com.bugshan.automative.group.app.repository.LignePanierRepository;
import com.bugshan.automative.group.app.repository.PanierRepository;
import com.bugshan.automative.group.app.repository.PieceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LignePanierService {

    private final LignePanierRepository lignePanierRepository;
    private final PanierRepository panierRepository;
    private final PieceRepository pieceRepository;

    public LignePanierDTO ajouterLigne(Long panierId, Long pieceId, int quantite, String livraison) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));
        Piece piece = pieceRepository.findById(pieceId)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée"));

        LignePanier ligne = new LignePanier();
        ligne.setPanier(panier);
        ligne.setPiece(piece);
        ligne.setQuantite(quantite);
        ligne.setLivraison(livraison);

        return new LignePanierDTO(lignePanierRepository.save(ligne));
    }

    public void supprimerLigne(Long ligneId) {
        lignePanierRepository.deleteById(ligneId);
    }
}

package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.LignePanier;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LignePanierDTO {

    private Long id;
    private Long pieceId;
    private String pieceNom;
    private String pieceImage;
    private double prixUnitaire;
    private int quantite;
    private String livraison;
    private double total;

    // Constructeur pour envoyer des données au front
    public LignePanierDTO(LignePanier ligne) {
        this.id = ligne.getId();
        this.pieceId = ligne.getPiece().getId();
        this.pieceNom = ligne.getPiece().getNom();
        this.pieceImage = ligne.getPiece().getImageUrl();
        this.prixUnitaire = ligne.getPiece().getPrix();
        this.quantite = ligne.getQuantite();
        this.livraison = ligne.getLivraison();
        this.total = prixUnitaire * quantite + (livraison.equalsIgnoreCase("express") ? 120 : 50);
    }

    // Constructeur pour recevoir une ligne depuis le front
    public LignePanierDTO(Long pieceId, int quantite, String livraison) {
        this.pieceId = pieceId;
        this.quantite = quantite;
        this.livraison = livraison;
    }

    public static LignePanierDTO fromEntity(LignePanier ligne) {
        LignePanierDTO dto = new LignePanierDTO();
        dto.setId(ligne.getId());
        dto.setPieceId(ligne.getPiece().getId());
        dto.setQuantite(ligne.getQuantite());
        dto.setLivraison(ligne.getLivraison());
        return dto;
    }
}

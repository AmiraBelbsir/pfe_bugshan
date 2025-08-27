package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.LigneCommande;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneCommandeDTO {

    private Long id;
    private Long pieceId;
    private int quantite;
    private String livraison;

    // Constructeur pour convertir directement depuis l'entité
    public LigneCommandeDTO(LigneCommande ligneCommande) {
        this.id = ligneCommande.getId();
        this.pieceId = ligneCommande.getPiece().getId();
        this.quantite = ligneCommande.getQuantite();
        this.livraison = ligneCommande.getLivraison();
    }
}

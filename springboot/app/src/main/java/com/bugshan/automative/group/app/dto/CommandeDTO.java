package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.Commande;
import com.bugshan.automative.group.app.model.LigneCommande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandeDTO {

    private Long id;
    private Long utilisateurId;
    private String status;
    private LocalDateTime dateCommande;
    private List<LigneCommandeDTO> lignes;

    public static CommandeDTO fromEntity(Commande commande) {
        return CommandeDTO.builder()
                .id(commande.getId())
                .utilisateurId(commande.getUtilisateur().getId())
                .status(commande.getStatut())
                .dateCommande(commande.getDateCommande())
                .lignes(commande.getLignes().stream()
                        .map(LigneCommandeDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LigneCommandeDTO {
        private Long id;
        private Long pieceId;
        private int quantite;
        private String livraison;

        public static LigneCommandeDTO fromEntity(LigneCommande ligne) {
            return LigneCommandeDTO.builder()
                    .id(ligne.getId())
                    .pieceId(ligne.getPiece().getId())
                    .quantite(ligne.getQuantite())
                    .livraison(ligne.getLivraison())
                    .build();
        }
    }
}

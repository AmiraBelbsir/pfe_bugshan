package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.Panier;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PanierDTO {

    private Long id;
    private boolean confirme;

    private Long utilisateurId;
    private String utilisateurNom;

    private List<LignePanierDTO> lignes;
    private double total;

    // Pour ajouter une seule ligne depuis le front
    private LignePanierDTO ligne;

    public PanierDTO(Panier panier) {
        this.id = panier.getId();
        this.confirme = panier.isConfirme();

        if (panier.getUtilisateur() != null) {
            this.utilisateurId = panier.getUtilisateur().getId();
            this.utilisateurNom = panier.getUtilisateur().getNomComplet();
        }

        this.lignes = panier.getLignes().stream()
                .map(LignePanierDTO::new)
                .collect(Collectors.toList());

        this.total = lignes.stream()
                .mapToDouble(LignePanierDTO::getTotal)
                .sum();
    }

    public static PanierDTO fromEntity(Panier panier) {
        PanierDTO dto = new PanierDTO();
        dto.setId(panier.getId());
        dto.setUtilisateurId(panier.getUtilisateur().getId());
        dto.setConfirme(panier.isConfirme());
        dto.setLignes(
                panier.getLignes().stream()
                        .map(LignePanierDTO::fromEntity)
                        .toList()
        );
        return dto;
    }
}

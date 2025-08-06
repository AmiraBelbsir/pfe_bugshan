package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UtilisateurDTO {
    private Long id;
    private String nomComplet;
    private String numeroTelephone;
    private String email;
    private Role role;
    private Sexe sexe;
    private String urlImage;
    private boolean actif;
    private String nomUtilisateur;
    private String adresse;
    private Ville ville;

    // ✅ Pièces fournies (sous forme de DTO simplifié)
    private List<PieceDTO> piecesFournies;

    public UtilisateurDTO(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.nomComplet = utilisateur.getNomComplet();
        this.numeroTelephone = utilisateur.getNumeroTelephone();
        this.email = utilisateur.getEmail();
        this.role = utilisateur.getRole();
        this.sexe = utilisateur.getSexe();
        this.urlImage = utilisateur.getUrlImage();
        this.actif = utilisateur.isActif();
        this.nomUtilisateur = utilisateur.getNomUtilisateur();
        this.adresse = utilisateur.getAdresse();
        this.ville = utilisateur.getVille();

        // ✅ Conversion des pièces fournies en DTOs
        if (utilisateur.getPieces() != null) {
            this.piecesFournies = utilisateur.getPieces().stream()
                    .map(PieceDTO::new) // Utilise le constructeur PieceDTO(Piece)
                    .collect(Collectors.toList());
        }
    }
}

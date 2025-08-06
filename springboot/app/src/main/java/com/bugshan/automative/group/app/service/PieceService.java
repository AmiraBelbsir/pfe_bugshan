package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.PieceDTO;
import com.bugshan.automative.group.app.model.*;
import com.bugshan.automative.group.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PieceService {

    private final PieceRepository pieceRepository;
    private final MagasinRepository magasinRepository;
    private final BlocRepository blocRepository;
    private final VehiculeRepository vehiculeRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public PieceService(
            PieceRepository pieceRepository,
            MagasinRepository magasinRepository,
            BlocRepository blocRepository,
            VehiculeRepository vehiculeRepository,
            UtilisateurRepository utilisateurRepository

    ) {
        this.pieceRepository = pieceRepository;
        this.magasinRepository = magasinRepository;
        this.blocRepository = blocRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    // ✅ Ajouter une pièce simple
    public Piece ajouterPiece(Piece piece) {
        return pieceRepository.save(piece);
    }

    public Piece ajouterPieceAvecRelations(PieceDTO pieceDTO) {
        Piece piece = new Piece();
        piece.setNom(pieceDTO.getNom());
        piece.setReference(pieceDTO.getReference());
        piece.setMarque(pieceDTO.getMarque());
        piece.setPrix(pieceDTO.getPrix());
        piece.setQuantite(pieceDTO.getQuantite());
        piece.setQuantiteMinimum(pieceDTO.getQuantiteMinimum());
        piece.setImageUrl(pieceDTO.getImageUrl());
        piece.setType(pieceDTO.getType());
        piece.setDescription(pieceDTO.getDescription());
        piece.setPrecommandable(pieceDTO.isPrecommandable());
        piece.setDateAchat(pieceDTO.getDateAchat());
        piece.setActive(pieceDTO.isActive());

        // 🔎 Récupération du Magasin
        Magasin magasin = magasinRepository.findById(pieceDTO.getMagasinId())
                .orElseThrow(() -> new RuntimeException("Magasin introuvable avec ID : " + pieceDTO.getMagasinId()));
        piece.setMagasin(magasin);

        // 🔎 Récupération du Bloc
        Bloc bloc = blocRepository.findById(pieceDTO.getBlocId())
                .orElseThrow(() -> new RuntimeException("Bloc introuvable avec ID : " + pieceDTO.getBlocId()));
        piece.setBloc(bloc);

        // 🔎 Récupération du fournisseur (Utilisateur)
        if (pieceDTO.getFournisseurId() != null) {
            Utilisateur fournisseur = utilisateurRepository.findById(pieceDTO.getFournisseurId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec ID : " + pieceDTO.getFournisseurId()));
            piece.setFournisseur(fournisseur);  // 👈 champ `fournisseur` de type Utilisateur dans Piece
        }

        // 🔁 Récupération des véhicules compatibles
        if (pieceDTO.getVehiculeIds() != null && !pieceDTO.getVehiculeIds().isEmpty()) {
            List<Vehicule> vehicules = vehiculeRepository.findAllById(pieceDTO.getVehiculeIds());
            piece.setVehiculesCompatibles(vehicules);
        }

        return pieceRepository.save(piece);
    }

    public Optional<Piece> findPieceById(Long id) {
        return pieceRepository.findById(id);
    }


    // ✅ Récupérer toutes les pièces
    public List<Piece> findAllPiece() {
        return pieceRepository.findAll();
    }
    public PieceDTO updatePiece(Long id, PieceDTO pieceDTO) {
        Piece piece = pieceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée avec l'id : " + id));

        // ➕ Mise à jour des champs simples
        piece.setNom(pieceDTO.getNom());
        piece.setReference(pieceDTO.getReference());
        piece.setMarque(pieceDTO.getMarque());
        piece.setPrix(pieceDTO.getPrix());
        piece.setQuantite(pieceDTO.getQuantite());
        piece.setQuantiteMinimum(pieceDTO.getQuantiteMinimum());
        piece.setDescription(pieceDTO.getDescription());
        piece.setType(pieceDTO.getType());
        piece.setPrecommandable(pieceDTO.isPrecommandable());
        piece.setDateAchat(pieceDTO.getDateAchat());
        piece.setImageUrl(pieceDTO.getImageUrl());
        piece.setActive(pieceDTO.isActive());

        // 🔁 Mise à jour du magasin
        if (pieceDTO.getMagasinId() != null) {
            Magasin magasin = magasinRepository.findById(pieceDTO.getMagasinId())
                    .orElseThrow(() -> new RuntimeException("Magasin non trouvé avec ID : " + pieceDTO.getMagasinId()));
            piece.setMagasin(magasin);
        }

        // 🔁 Mise à jour du bloc
        if (pieceDTO.getBlocId() != null) {
            Bloc bloc = blocRepository.findById(pieceDTO.getBlocId())
                    .orElseThrow(() -> new RuntimeException("Bloc non trouvé avec ID : " + pieceDTO.getBlocId()));
            piece.setBloc(bloc);
        }

        // 🔁 Mise à jour du fournisseur (Utilisateur)
        if (pieceDTO.getFournisseurId() != null) {
            Utilisateur fournisseur = utilisateurRepository.findById(pieceDTO.getFournisseurId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur (fournisseur) non trouvé avec ID : " + pieceDTO.getFournisseurId()));
            piece.setFournisseur(fournisseur);
        } else {
            piece.setFournisseur(null); // permet de supprimer le fournisseur s'il est retiré
        }

        // 🔁 Mise à jour des véhicules compatibles
        if (pieceDTO.getVehiculeIds() != null) {
            List<Vehicule> vehicules = vehiculeRepository.findAllById(pieceDTO.getVehiculeIds());
            piece.setVehiculesCompatibles(vehicules);
        }

        // 💾 Enregistrer en base
        Piece updatedPiece = pieceRepository.save(piece);

        return new PieceDTO(updatedPiece);
    }

}


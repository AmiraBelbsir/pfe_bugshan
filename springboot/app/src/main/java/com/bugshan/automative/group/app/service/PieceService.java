package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.dto.PieceDTO;
import com.bugshan.automative.group.app.model.Bloc;
import com.bugshan.automative.group.app.model.Magasin;
import com.bugshan.automative.group.app.model.Piece;
import com.bugshan.automative.group.app.model.User;
import com.bugshan.automative.group.app.repository.BlocRepository;
import com.bugshan.automative.group.app.repository.MagasinRepository;
import com.bugshan.automative.group.app.repository.PieceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PieceService {

    private final PieceRepository pieceRepository;
    private final MagasinRepository magasinRepository;
    private final BlocRepository blocRepository;

    @Autowired
    public PieceService(
            PieceRepository pieceRepository,
            MagasinRepository magasinRepository,
            BlocRepository blocRepository
    ) {
        this.pieceRepository = pieceRepository;
        this.magasinRepository = magasinRepository;
        this.blocRepository = blocRepository;
    }

    // ✅ Ajouter une pièce simple
    public Piece ajouterPiece(Piece piece) {
        return pieceRepository.save(piece);
    }

    // ✅ Ajouter une pièce avec Magasin + Bloc
    public Piece ajouterPieceAvecRelations(PieceDTO pieceDTO) {
        Piece piece = new Piece();
        piece.setNom(pieceDTO.getNom());
        piece.setReference(pieceDTO.getReference());
        piece.setMarque(pieceDTO.getMarque());
        piece.setPrix(pieceDTO.getPrix());
        piece.setQuantite(pieceDTO.getQuantite());
        piece.setImageUrl(pieceDTO.getImageUrl());
        piece.setType(pieceDTO.getType());
        piece.setDescription(pieceDTO.getDescription());
        piece.setCompatibilite(pieceDTO.getCompatibilite());
        piece.setPrecommandable(pieceDTO.isPrecommandable());

        // 🔎 Récupération du Magasin
        Optional<Magasin> magasinOpt = magasinRepository.findById(pieceDTO.getMagasinId());
        if (magasinOpt.isPresent()) {
            piece.setMagasin(magasinOpt.get());
        } else {
            throw new RuntimeException("Magasin introuvable avec ID : " + pieceDTO.getMagasinId());
        }

        // 🔎 Récupération du Bloc
        Optional<Bloc> blocOpt = blocRepository.findById(pieceDTO.getBlocId());
        if (blocOpt.isPresent()) {
            piece.setBloc(blocOpt.get());
        } else {
            throw new RuntimeException("Bloc introuvable avec ID : " + pieceDTO.getBlocId());
        }

        // 💾 Sauvegarde de la pièce
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
        piece.setCompatibilite(pieceDTO.getCompatibilite());
        piece.setType(pieceDTO.getType());
        piece.setPrecommandable(pieceDTO.isPrecommandable());
        piece.setDateAchat(pieceDTO.getDateAchat());
        piece.setImageUrl(pieceDTO.getImageUrl());

        // 🔁 Mise à jour du magasin s'il est différent
        if (pieceDTO.getMagasinId() != null) {
            Magasin magasin = magasinRepository.findById(pieceDTO.getMagasinId())
                    .orElseThrow(() -> new RuntimeException("Magasin non trouvé"));
            piece.setMagasin(magasin);
        }

        // 🔁 Mise à jour du bloc s'il est différent
        if (pieceDTO.getBlocId() != null) {
            Bloc bloc = blocRepository.findById(pieceDTO.getBlocId())
                    .orElseThrow(() -> new RuntimeException("Bloc non trouvé"));
            piece.setBloc(bloc);
        }

        // 💾 Enregistrer en base de données
        Piece updatedPiece = pieceRepository.save(piece);

        // 🔁 Retourner un DTO mis à jour
        return new PieceDTO(updatedPiece);
    }

}


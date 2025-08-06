package com.bugshan.automative.group.app.controller;


import com.bugshan.automative.group.app.dto.PieceDTO;

import com.bugshan.automative.group.app.model.*;
import com.bugshan.automative.group.app.repository.*;

import com.bugshan.automative.group.app.service.PieceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/piece")
@CrossOrigin(origins = "http://localhost:4200")// Autorise Angular à appeler ce contrôleur



public class PieceController {

    @Autowired
    private PieceService pieceService;
    @Autowired
    private PieceRepository pieceRepository;
    @Autowired
    private MagasinRepository magasinRepository;

    @Autowired
    private BlocRepository blocRepository;
    @Autowired
    private  VehiculeRepository vehiculeRepository;
    @Autowired
    private  UtilisateurRepository utilisateurRepository;
    @GetMapping
    public List<PieceDTO> getAllPieces() {
        return pieceService.findAllPiece().stream()
                .map(PieceDTO::new)
                .collect(Collectors.toList());
}
    @PostMapping
    public ResponseEntity<?> createPieceWithImage(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("nom") String nom,
            @RequestParam("reference") String reference,
            @RequestParam("marque") String marque,
            @RequestParam("prix") double prix,
            @RequestParam("quantite") int quantite,
            @RequestParam("type") TypePiece type,
            @RequestParam("dateAchat") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAchat,
            @RequestParam("quantiteMinimum") int quantiteMinimum,
            @RequestParam("description") String description,
            @RequestParam("precommandable") boolean precommandable,
            @RequestParam("active") boolean active,
            @RequestParam("magasinId") Long magasinId,
            @RequestParam("blocId") Long blocId,
            @RequestParam("fournisseurId") Long fournisseurId,
            @RequestParam(value = "vehiculeIds", required = false) List<Long> vehiculeIds
    ) {
        try {
            String imageUrl = null;

            // 📷 Gestion de l’image
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                imageUrl = fileName;
            }

            // 🧱 Construction du DTO
            PieceDTO pieceDTO = new PieceDTO();
            pieceDTO.setNom(nom);
            pieceDTO.setReference(reference);
            pieceDTO.setMarque(marque);
            pieceDTO.setPrix(prix);
            pieceDTO.setQuantite(quantite);
            pieceDTO.setType(type);
            pieceDTO.setDateAchat(dateAchat);
            pieceDTO.setQuantiteMinimum(quantiteMinimum);
            pieceDTO.setDescription(description);
            pieceDTO.setPrecommandable(precommandable);
            pieceDTO.setImageUrl(imageUrl);
            pieceDTO.setActive(active);
            pieceDTO.setMagasinId(magasinId);
            pieceDTO.setBlocId(blocId);
            pieceDTO.setFournisseurId(fournisseurId);
            pieceDTO.setVehiculeIds(vehiculeIds);

            // 💾 Création
            Piece savedPiece = pieceService.ajouterPieceAvecRelations(pieceDTO);

            return ResponseEntity.ok(savedPiece);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("L'upload de l'image a échoué");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la création de la pièce : " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePiece(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "marque", required = false) String marque,
            @RequestParam(value = "prix", required = false) Double prix,
            @RequestParam(value = "quantite", required = false) Integer quantite,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "quantiteMinimum", required = false) Integer quantiteMinimum,
            @RequestParam(value = "type", required = false) TypePiece type,
            @RequestParam(value = "dateAchat", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAchat,
            @RequestParam(value = "precommandable", required = false) Boolean precommandable,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "magasinId", required = false) Long magasinId,
            @RequestParam(value = "blocId", required = false) Long blocId,
            @RequestParam(value = "fournisseurId", required = false) Long fournisseurId,
            @RequestParam(value = "vehiculeIds", required = false) List<Long> vehiculeIds
    ) {
        Optional<Piece> optionalPiece = pieceRepository.findById(id);
        if (optionalPiece.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pièce non trouvée.");
        }

        Piece piece = optionalPiece.get();

        if (nom != null) piece.setNom(nom);
        if (reference != null) piece.setReference(reference);
        if (marque != null) piece.setMarque(marque);
        if (prix != null) piece.setPrix(prix);
        if (quantite != null) piece.setQuantite(quantite);
        if (description != null) piece.setDescription(description);
        if (quantiteMinimum != null) piece.setQuantiteMinimum(quantiteMinimum);
        if (active != null) piece.setActive(active);
        if (type != null) piece.setType(type);
        if (dateAchat != null) piece.setDateAchat(dateAchat);
        if (precommandable != null) piece.setPrecommandable(precommandable);

        if (magasinId != null) {
            Optional<Magasin> optionalMagasin = magasinRepository.findById(magasinId);
            if (optionalMagasin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Magasin non trouvé");
            }
            piece.setMagasin(optionalMagasin.get());
        }

        if (blocId != null) {
            Optional<Bloc> optionalBloc = blocRepository.findById(blocId);
            if (optionalBloc.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bloc non trouvé");
            }
            piece.setBloc(optionalBloc.get());
        }

        // ✅ Fournisseur (Utilisateur)
        if (fournisseurId != null) {
            Optional<Utilisateur> optionalFournisseur = utilisateurRepository.findById(fournisseurId);
            if (optionalFournisseur.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fournisseur (utilisateur) non trouvé");
            }
            piece.setFournisseur(optionalFournisseur.get());
        }

        // ✅ Véhicules compatibles
        if (vehiculeIds != null && !vehiculeIds.isEmpty()) {
            List<Vehicule> vehicules = vehiculeRepository.findAllById(vehiculeIds);
            piece.setVehiculesCompatibles(vehicules);
        }

        // 📷 Upload de l’image
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                piece.setImageUrl(fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'upload de l'image.");
            }
        }

        // 💾 Sauvegarde
        Piece updated = pieceRepository.save(piece);
        return ResponseEntity.ok(new PieceDTO(updated));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PieceDTO> getPieceById(@PathVariable Long id) {
        Optional<Piece> pieceOptional = pieceRepository.findById(id);

        if (pieceOptional.isPresent()) {
            Piece piece = pieceOptional.get();

            PieceDTO dto = new PieceDTO();
            dto.setId(piece.getId());
            dto.setNom(piece.getNom());
            dto.setReference(piece.getReference());
            dto.setMarque(piece.getMarque());
            dto.setPrix(piece.getPrix());
            dto.setQuantite(piece.getQuantite());
            dto.setQuantiteMinimum(piece.getQuantiteMinimum());
            dto.setDescription(piece.getDescription());
            dto.setType(piece.getType());
            dto.setDateAchat(piece.getDateAchat());
            dto.setPrecommandable(piece.isPrecommandable());
            dto.setActive(piece.isActive());
            dto.setImageUrl(piece.getImageUrl());

            // Magasin
            if (piece.getMagasin() != null) {
                dto.setMagasinId(piece.getMagasin().getId());
                dto.setMagasinNom(piece.getMagasin().getNom());
            }

            // Bloc
            if (piece.getBloc() != null) {
                dto.setBlocId(piece.getBloc().getId());
                dto.setBlocNom(piece.getBloc().getNom());
            }

            // Fournisseur (Utilisateur)
            if (piece.getFournisseur() != null) {
                dto.setFournisseurId(piece.getFournisseur().getId());
                dto.setFournisseurNom(piece.getFournisseur().getNomComplet()); // ou getUsername() selon ton modèle
            }

            // Véhicules compatibles
            if (piece.getVehiculesCompatibles() != null && !piece.getVehiculesCompatibles().isEmpty()) {
                List<Long> vehiculeIds = piece.getVehiculesCompatibles()
                        .stream()
                        .map(Vehicule::getId)
                        .collect(Collectors.toList());
                dto.setVehiculeIds(vehiculeIds);
            }

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}






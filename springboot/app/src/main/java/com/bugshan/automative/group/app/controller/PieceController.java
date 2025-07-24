package com.bugshan.automative.group.app.controller;


import com.bugshan.automative.group.app.dto.PieceDTO;
import com.bugshan.automative.group.app.dto.UserDTO;
import com.bugshan.automative.group.app.model.*;
import com.bugshan.automative.group.app.repository.BlocRepository;
import com.bugshan.automative.group.app.repository.MagasinRepository;
import com.bugshan.automative.group.app.repository.PieceRepository;
import com.bugshan.automative.group.app.repository.UserRepository;
import com.bugshan.automative.group.app.service.PieceService;
import com.bugshan.automative.group.app.service.UserService;
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
            @RequestParam(value = "imageUrl", required = false) String imageUrl, // optionnel
            @RequestParam("type") TypePiece type,
            @RequestParam("dateAchat") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAchat,
            @RequestParam("quantiteMinimum") int quantiteMinimum,
            @RequestParam("description") String description,
            @RequestParam("compatibilite") String compatibilite,
            @RequestParam("precommandable") boolean precommandable,
            @RequestParam("magasinId") Long magasinId,       // <-- ajouté
            @RequestParam("blocId") Long blocId              // <-- ajouté
    ) {
        try {
            // Gestion du fichier image uploadé
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get("uploads", fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());

                imageUrl = fileName; // on remplace imageUrl par le nom du fichier uploadé
            }

            // Création de PieceDTO à partir des paramètres
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
            pieceDTO.setCompatibilite(compatibilite);
            pieceDTO.setPrecommandable(precommandable);
            pieceDTO.setImageUrl(imageUrl);
            pieceDTO.setMagasinId(magasinId);
            pieceDTO.setBlocId(blocId);

            // Appel du service avec DTO
            Piece savedPiece = pieceService.ajouterPieceAvecRelations(pieceDTO);

            return ResponseEntity.ok(savedPiece);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("L'upload de l'image a échoué");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la création de la pièce : " + e.getMessage());
        }
    }
    @PutMapping("/{id}")  // Cette méthode sera appelée quand on fait une requête PUT sur /api/pieces/{id}
    public ResponseEntity<?> updatePiece(
            @PathVariable Long id,  // On récupère l’ID de la pièce à modifier
            @RequestParam(value = "file", required = false) MultipartFile file,  // Image facultative
            @RequestParam(value = "nom", required = false) String nom,
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "marque", required = false) String marque,
            @RequestParam(value = "prix", required = false) Double prix,
            @RequestParam(value = "quantite", required = false) Integer quantite,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "compatibilite", required = false) String compatibilite,
            @RequestParam(value = "quantiteMinimum", required = false) Integer quantiteMinimum,
            @RequestParam(value = "type", required = false) TypePiece type,
            @RequestParam(value = "dateAchat", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateAchat,
            @RequestParam(value = "precommandable", required = false) Boolean precommandable,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "magasinId", required = false) Long magasinId,  // ID du magasin associé
            @RequestParam(value = "blocId", required = false) Long blocId , // ID du bloc associé
            @RequestParam(value = "magasinNom", required = false) Long magasinNom ,
            @RequestParam(value = "blocNom", required = false) Long blocNom

    ) {
        // 1. On vérifie que la pièce avec l’ID existe
        Optional<Piece> optionalPiece = pieceRepository.findById(id);
        if (optionalPiece.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pièce non trouvée.");
        }

        // 2. On récupère l’objet existant
        Piece piece = optionalPiece.get();

        // 3. Mise à jour des champs si des nouvelles valeurs sont envoyées
        if (nom != null) piece.setNom(nom);
        if (reference != null) piece.setReference(reference);
        if (marque != null) piece.setMarque(marque);
        if (prix != null) piece.setPrix(prix);
        if (quantite != null) piece.setQuantite(quantite);
        if (description != null) piece.setDescription(description);
        if (compatibilite != null) piece.setCompatibilite(compatibilite);
        if (quantiteMinimum != null) piece.setQuantiteMinimum(quantiteMinimum);
        if (active != null) piece.setActive(active);
        if (type != null) piece.setType( type);
        if (dateAchat != null) piece.setDateAchat(dateAchat);
        if (precommandable != null) piece.setPrecommandable(precommandable);

        // 4. Mise à jour du magasin associé si l’ID est fourni
        if (magasinId != null) {
            Optional<Magasin> optionalMagasin = magasinRepository.findById(magasinId);
            if (optionalMagasin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Magasin non trouvé");
            }
            piece.setMagasin(optionalMagasin.get());
        }

        // 5. Mise à jour du bloc associé si l’ID est fourni
        if (blocId != null) {
            Optional<Bloc> optionalBloc = blocRepository.findById(blocId);
            if (optionalBloc.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bloc non trouvé");
            }
            piece.setBloc(optionalBloc.get());
        }

        // 6. Traitement du fichier image si une nouvelle image est envoyée
        if (file != null && !file.isEmpty()) {
            try {
                // On génère un nom de fichier unique
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                // On définit le chemin du dossier uploads
                Path filePath = Paths.get("uploads", fileName);

                // On crée les dossiers si nécessaire
                Files.createDirectories(filePath.getParent());

                // On écrit le fichier sur le disque
                Files.write(filePath, file.getBytes());

                // On enregistre le chemin de l’image dans la pièce
                piece.setImageUrl(fileName);

            } catch (IOException e) {
                // En cas d’erreur pendant l’upload
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'upload de l'image.");
            }
        }

        // 7. Enregistrement des modifications dans la base de données
        Piece updated = pieceRepository.save(piece);

        // 8. On retourne la pièce mise à jour en réponse
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
            dto.setCompatibilite(piece.getCompatibilite());
            dto.setType(piece.getType());
            dto.setDateAchat(piece.getDateAchat());
            dto.setPrecommandable(piece.isPrecommandable());
            dto.setActive(piece.isActive());
            dto.setImageUrl(piece.getImageUrl());
            dto.setMagasinId(piece.getMagasin() != null ? piece.getMagasin().getId() : null);
            dto.setBlocId(piece.getBloc() != null ? piece.getBloc().getId() : null);
            dto.setMagasinNom(piece.getMagasin().getNom());
            dto.setBlocNom(piece.getBloc().getNom());



            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}






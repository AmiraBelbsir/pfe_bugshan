package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.UtilisateurDTO;

import com.bugshan.automative.group.app.model.Role;
import com.bugshan.automative.group.app.model.Sexe;
import com.bugshan.automative.group.app.model.Utilisateur;
import com.bugshan.automative.group.app.model.Ville;

import com.bugshan.automative.group.app.repository.UtilisateurRepository;
import com.bugshan.automative.group.app.service.UtilisateurService;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UtilisateurService userService;
    @Autowired
    private UtilisateurRepository userRepository;

    @GetMapping
    public List<UtilisateurDTO> getUsers() {
        return userService.findAllUsers().stream()
                .map(UtilisateurDTO::new)
                .collect(Collectors.toList());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUserById(@PathVariable Long id) {
        Optional<Utilisateur> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> creerUtilisateurAvecImage(
            @RequestParam(value = "fichier", required = false) MultipartFile fichier,
            @RequestParam("nomComplet") String nomComplet,
            @RequestParam("email") String email,
            @RequestParam("nomUtilisateur") String nomUtilisateur,
            @RequestParam("motDePasse") String motDePasse,
            @RequestParam("numeroTelephone") String numeroTelephone,
            @RequestParam("sexe") String sexe,
            @RequestParam("role") String role,
            @RequestParam("ville") String ville,
            @RequestParam("adresse") String adresse) {

        if (userService.existsByUsername(nomUtilisateur)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("champ", "nomUtilisateur", "message", "Ce nom d'utilisateur est déjà utilisé."));
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("champ", "email", "message", "Cet email est déjà utilisé."));
        }

        if (userService.existsByPhone(numeroTelephone)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("champ", "numeroTelephone", "message", "Ce numéro est déjà utilisé."));
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomComplet(nomComplet);
        utilisateur.setEmail(email);
        utilisateur.setNomUtilisateur(nomUtilisateur);
        utilisateur.setMotDePasse(motDePasse);  // mot de passe en clair
        utilisateur.setNumeroTelephone(numeroTelephone);
        utilisateur.setSexe(Sexe.valueOf(sexe.toUpperCase()));
        utilisateur.setRole(Role.valueOf(role.toUpperCase()));
        utilisateur.setVille(Ville.valueOf(ville.toUpperCase()));
        utilisateur.setAdresse(adresse);
        utilisateur.setActif(true);

        try {
            if (fichier != null && !fichier.isEmpty()) {
                String nomFichier = UUID.randomUUID() + "_" + fichier.getOriginalFilename();
                Path cheminFichier = Paths.get("uploads", nomFichier);
                Files.createDirectories(cheminFichier.getParent());
                Files.write(cheminFichier, fichier.getBytes());
                utilisateur.setUrlImage(nomFichier);
            }

            Utilisateur utilisateurSauvegarde = userService.addUser(utilisateur);
            return ResponseEntity.ok(new UtilisateurDTO(utilisateurSauvegarde));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec du téléchargement de l'image");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> mettreAJourUtilisateur(
            @PathVariable Long id,
            @RequestParam(value = "fichier", required = false) MultipartFile fichier,
            @RequestParam(value = "nomComplet", required = false) String nomComplet,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "nomUtilisateur", required = false) String nomUtilisateur,
            @RequestParam(value = "motDePasse", required = false) String motDePasse,
            @RequestParam(value = "numeroTelephone", required = false) String numeroTelephone,
            @RequestParam(value = "sexe", required = false) String sexe,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "ville", required = false) String ville,
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "actif", required = false) Boolean actif) {

        Optional<Utilisateur> utilisateurOpt = userRepository.findById(id);
        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (nomUtilisateur != null && !utilisateur.getNomUtilisateur().equals(nomUtilisateur)) {
            if (userRepository.findByNomUtilisateur(nomUtilisateur).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("champ", "nomUtilisateur", "message", "Ce nom d'utilisateur est déjà utilisé."));
            }
        }
        if (email != null && !utilisateur.getEmail().equals(email)) {
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("champ", "email", "message", "Cet email est déjà utilisé."));
            }
        }
        if (numeroTelephone != null && !utilisateur.getNumeroTelephone().equals(numeroTelephone)) {
            if (userRepository.findByNumeroTelephone(numeroTelephone).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("champ", "numeroTelephone", "message", "Ce numéro de téléphone est déjà utilisé."));
            }
        }

        if (nomComplet != null) utilisateur.setNomComplet(nomComplet);
        if (email != null) utilisateur.setEmail(email);
        if (nomUtilisateur != null) utilisateur.setNomUtilisateur(nomUtilisateur);
        if (numeroTelephone != null) utilisateur.setNumeroTelephone(numeroTelephone);
        if (motDePasse != null) utilisateur.setMotDePasse(motDePasse);  // mot de passe en clair
        if (role != null) utilisateur.setRole(Role.valueOf(role.toUpperCase()));
        if (sexe != null) utilisateur.setSexe(Sexe.valueOf(sexe.toUpperCase()));
        if (ville != null) utilisateur.setVille(Ville.valueOf(ville.toUpperCase()));
        if (adresse != null) utilisateur.setAdresse(adresse);
        if (actif != null) utilisateur.setActif(actif);

        try {
            if (fichier != null && !fichier.isEmpty()) {
                String nomFichier = UUID.randomUUID() + "_" + fichier.getOriginalFilename();
                Path cheminFichier = Paths.get("uploads", nomFichier);
                Files.createDirectories(cheminFichier.getParent());
                Files.write(cheminFichier, fichier.getBytes());
                utilisateur.setUrlImage(nomFichier);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec du téléchargement de l'image");
        }

        userRepository.save(utilisateur);
        return ResponseEntity.ok(new UtilisateurDTO(utilisateur));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur utilisateur) {
        UtilisateurDTO foundUser = userService.findByEmailAndPassword(utilisateur.getEmail(), utilisateur.getMotDePasse());

        if (foundUser != null) {
            // Return user type with the DTO
            return ResponseEntity.ok().body(foundUser);
        } else {
            // Return 401 if credentials are invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/cities")
    public List<String> getAllCities() {
        return Arrays.stream(Ville.values()).map(Enum::name).toList();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserField(
            @PathVariable Long id,
            @RequestBody Map<String, String> update
    ) {
        String field = update.get("field");
        String value = update.get("value");

        if (field == null || value == null) {
            return ResponseEntity.badRequest().body("Champ ou valeur manquant.");
        }

        Optional<Utilisateur> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }

        Utilisateur existingUser = existingUserOpt.get();

        switch (field) {
            case "nomUtilisateur":
                if (!existingUser.getNomUtilisateur().equals(value)) {
                    Optional<Utilisateur> usernameExists = userRepository.findByNomUtilisateur(value);
                    if (usernameExists.isPresent()) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("field", "nomUtilisateur", "message", "Ce nom d'utilisateur est déjà utilisé."));
                    }
                }
                break;

            case "email":
                if (!existingUser.getEmail().equals(value)) {
                    Optional<Utilisateur> emailExists = userRepository.findByEmail(value);
                    if (emailExists.isPresent()) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("field", "email", "message", "Cet email est déjà utilisé."));
                    }
                }
                break;

            case "numeroTelephone":
                if (!existingUser.getNumeroTelephone().equals(value)) {
                    Optional<Utilisateur> gsmExists = userRepository.findByNumeroTelephone(value);
                    if (gsmExists.isPresent()) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("field", "numeroTelephone", "message", "Ce numéro de téléphone est déjà utilisé."));
                    }
                }
                break;




        }

        try {
            Utilisateur updated = userService.updateUserField(id, field, value);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour.");
        }
    }


    @PutMapping("/deactivate/{id}")
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateAcc(id);
    }
}

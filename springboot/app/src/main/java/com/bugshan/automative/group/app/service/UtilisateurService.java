package com.bugshan.automative.group.app.service;


import com.bugshan.automative.group.app.dto.UtilisateurDTO;

import com.bugshan.automative.group.app.model.Utilisateur;

import com.bugshan.automative.group.app.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository userRepository;

    public UtilisateurService(UtilisateurRepository userRepository) {

        this.userRepository = userRepository;
    }

    public Utilisateur addUser(Utilisateur user) {
        return userRepository.save(user);
    }

    // Find a user by ID
    public Optional<Utilisateur> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Find a user by email
    public Optional<Utilisateur> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UtilisateurDTO findByEmailAndPassword(String email, String password) {
        Optional<Utilisateur> utilisateurOpt = userRepository.findByEmail(email);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            if (utilisateur.getMotDePasse().equals(password)) {
                return new UtilisateurDTO(utilisateur); // Renvoie un DTO à la place de l'entité
            }
        }
        return null;
    }

    // Get all users
    public List<Utilisateur> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByNomUtilisateur(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public boolean existsByPhone(String gsm) {
        return userRepository.existsByNumeroTelephone(gsm);
    }

}
package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    boolean existsByNomUtilisateur(String nomUtilisateur);

    boolean existsByEmail(String email);

    boolean existsByNumeroTelephone(String numeroTelephone);

    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);

    Optional<Utilisateur> findByNumeroTelephone(String numeroTelephone);
}

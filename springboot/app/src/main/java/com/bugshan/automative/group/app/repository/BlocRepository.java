package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {
    // Méthode qui récupère tous les blocs d'un magasin donné par son id
    List<Bloc> findByMagasinId(Long magasinId);
}
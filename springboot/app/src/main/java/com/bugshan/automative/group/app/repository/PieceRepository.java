package com.bugshan.automative.group.app.repository;



import com.bugshan.automative.group.app.model.Piece;
import com.bugshan.automative.group.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Long>{
    Optional<Piece> findByNom(String nom);
    Optional<Piece> findByReference(String ref);
}

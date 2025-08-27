package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.LignePanier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LignePanierRepository extends JpaRepository<LignePanier, Long> {

    List<LignePanier> findByPanierId(Long panierId);

    void deleteByPanierId(Long panierId);
}

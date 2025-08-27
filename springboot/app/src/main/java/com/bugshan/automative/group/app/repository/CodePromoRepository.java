package com.bugshan.automative.group.app.repository;

import com.bugshan.automative.group.app.model.CodePromo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodePromoRepository extends JpaRepository<CodePromo, Long> {
    Optional<CodePromo> findByCodeIgnoreCaseAndActiveTrue(String code);
}

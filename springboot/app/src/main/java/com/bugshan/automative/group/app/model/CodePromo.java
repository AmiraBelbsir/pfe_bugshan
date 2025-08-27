package com.bugshan.automative.group.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_promo", uniqueConstraints = @UniqueConstraint(name = "uk_code_promo_code", columnNames = "code"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CodePromo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String code;                     // ex: "REDUCTION10" (stocké en MAJ)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DiscountType type;               // PERCENTAGE ou FIXED

    @Column(nullable = false)
    private double amount;                   // % si PERCENTAGE, valeur en MAD si FIXED

    private Double maxDiscount;              // plafond pour un pourcentage (optionnel)

    private Double minOrderTotal;            // montant minimum panier requis (optionnel)

    private LocalDateTime startsAt;          // période de validité (optionnel)
    private LocalDateTime endsAt;

    @Column(nullable = false)
    private boolean active = true;

    private Integer globalUsageLimit;        // nombre total d’utilisations possible (optionnel)
    @Column(nullable = false)
    private int usedCount = 0;               // compteur global utilisé

    private Integer perUserLimit;            // nb max par utilisateur (optionnel)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (code != null) code = code.toUpperCase().trim();
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        if (code != null) code = code.toUpperCase().trim();
        updatedAt = LocalDateTime.now();
    }

    public enum DiscountType { PERCENTAGE, FIXED }
}

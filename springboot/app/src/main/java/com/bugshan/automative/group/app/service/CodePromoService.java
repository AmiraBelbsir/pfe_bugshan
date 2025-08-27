package com.bugshan.automative.group.app.service;

import com.bugshan.automative.group.app.model.CodePromo;
import com.bugshan.automative.group.app.model.Panier;
import com.bugshan.automative.group.app.repository.CodePromoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CodePromoService {

    private final CodePromoRepository codePromoRepository;

    public record ApplyResult(double discountAmount, double newTotal, CodePromo promo) {}

    public ApplyResult validateAndApply(String rawCode, Panier panier, Long utilisateurId, double currentTotal) {
        CodePromo promo = codePromoRepository.findByCodeIgnoreCaseAndActiveTrue(rawCode)
                .orElseThrow(() -> new RuntimeException("Code promo invalide ou inactif"));

        LocalDateTime now = LocalDateTime.now();
        if (promo.getStartsAt() != null && now.isBefore(promo.getStartsAt())) {
            throw new RuntimeException("Code promo pas encore actif");
        }
        if (promo.getEndsAt() != null && now.isAfter(promo.getEndsAt())) {
            throw new RuntimeException("Code promo expiré");
        }
        if (promo.getMinOrderTotal() != null && currentTotal < promo.getMinOrderTotal()) {
            throw new RuntimeException("Montant minimum non atteint");
        }
        if (promo.getGlobalUsageLimit() != null && promo.getUsedCount() >= promo.getGlobalUsageLimit()) {
            throw new RuntimeException("Code promo épuisé");
        }
        // NOTE: perUserLimit nécessite un suivi par utilisateur (table d'usages). À ajouter si besoin.

        double discount;
        if (promo.getType() == CodePromo.DiscountType.PERCENTAGE) {
            discount = currentTotal * (promo.getAmount() / 100.0);
            if (promo.getMaxDiscount() != null) {
                discount = Math.min(discount, promo.getMaxDiscount());
            }
        } else {
            discount = promo.getAmount();
        }

        if (discount < 0) discount = 0;
        double newTotal = Math.max(0, currentTotal - discount);

        // Incrémenter l'usage global (optionnel: déplacer au moment de confirmation commande)
        promo.setUsedCount(promo.getUsedCount() + 1);
        codePromoRepository.save(promo);

        return new ApplyResult(discount, newTotal, promo);
    }
}

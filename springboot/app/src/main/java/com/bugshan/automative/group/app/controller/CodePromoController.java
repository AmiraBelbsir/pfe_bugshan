package com.bugshan.automative.group.app.controller;

import com.bugshan.automative.group.app.dto.PanierDTO;
import com.bugshan.automative.group.app.model.Panier;
import com.bugshan.automative.group.app.repository.PanierRepository;
import com.bugshan.automative.group.app.service.CodePromoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/codes-promo")
@RequiredArgsConstructor
public class CodePromoController {

    private final CodePromoService codePromoService;
    private final PanierRepository panierRepository;

    @PostMapping("/apply")
    public ResponseEntity<ApplyResponse> apply(@RequestBody ApplyRequest req) {
        Panier panier = panierRepository.findById(req.getPanierId())
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // total courant : somme des lignes (prix * qte) + frais de livraison ligne (si présents)
        double currentTotal = panier.getLignes().stream().mapToDouble(l ->
                l.getPiece().getPrix() * l.getQuantite()
                        + ("express".equalsIgnoreCase(l.getLivraison()) ? 120 : 50)
        ).sum();

        var result = codePromoService.validateAndApply(req.getCode(), panier, req.getUtilisateurId(), currentTotal);

        ApplyResponse resp = new ApplyResponse();
        resp.setPanier(new PanierDTO(panier));     // panier “as-is”
        resp.setDiscountAmount(result.discountAmount());
        resp.setNewTotal(result.newTotal());
        resp.setAppliedCode(req.getCode().toUpperCase());

        return ResponseEntity.ok(resp);
    }

    @Data
    public static class ApplyRequest {
        private Long panierId;
        private Long utilisateurId;
        private String code;
    }

    @Data
    public static class ApplyResponse {
        private PanierDTO panier;
        private String appliedCode;
        private double discountAmount;
        private double newTotal;
    }
}

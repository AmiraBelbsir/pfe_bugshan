package com.bugshan.automative.group.app.dto;

import com.bugshan.automative.group.app.model.CodePromo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodePromoDTO {

    private Long id;
    private String code;
    private String type;           // PERCENTAGE ou FIXED
    private double amount;
    private Double maxDiscount;
    private Double minOrderTotal;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private boolean active;
    private Integer globalUsageLimit;
    private int usedCount;
    private Integer perUserLimit;

    public static CodePromoDTO fromEntity(CodePromo promo) {
        return CodePromoDTO.builder()
                .id(promo.getId())
                .code(promo.getCode())
                .type(promo.getType().name())
                .amount(promo.getAmount())
                .maxDiscount(promo.getMaxDiscount())
                .minOrderTotal(promo.getMinOrderTotal())
                .startsAt(promo.getStartsAt())
                .endsAt(promo.getEndsAt())
                .active(promo.isActive())
                .globalUsageLimit(promo.getGlobalUsageLimit())
                .usedCount(promo.getUsedCount())
                .perUserLimit(promo.getPerUserLimit())
                .build();
    }
}

package com.example.subscription.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseInfo {

    private Long memberId;
    private Long passProductId;
    private int quantity;

    @Builder
    private PurchaseInfo(Long memberId, Long passProductId, int quantity) {
        this.memberId = memberId;
        this.passProductId = passProductId;
        this.quantity = quantity;
    }
}

package com.example.subscription.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseInfo {

    private Long memberId;
    private Long subscriptionProductId;
    private int quantity;

    @Builder
    private PurchaseInfo(Long memberId, Long subscriptionProductId, int quantity) {
        this.memberId = memberId;
        this.subscriptionProductId = subscriptionProductId;
        this.quantity = quantity;
    }
}

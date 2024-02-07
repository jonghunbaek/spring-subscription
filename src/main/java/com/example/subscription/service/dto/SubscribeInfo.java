package com.example.subscription.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SubscribeInfo {

    private Long memberId;
    private Long subscriptionProductId;
    private int quantity;

    @Builder
    private SubscribeInfo(Long memberId, Long subscriptionProductId, int quantity) {
        this.memberId = memberId;
        this.subscriptionProductId = subscriptionProductId;
        this.quantity = quantity;
    }
}

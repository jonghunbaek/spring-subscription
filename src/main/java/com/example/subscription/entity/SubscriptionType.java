package com.example.subscription.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum SubscriptionType {
    PERIOD("기간제 구독권"),
    FREE_SINGLE("무료 단건 구독권"),
    PAID_SINGLE("유료 단건 구독권");

    private String description;

    SubscriptionType(String description) {
        this.description = description;
    }
}


package com.example.subscription.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum PassType {
    SUBSCRIPTION("기간제 이용권"),
    PAID_CONSUMABLE("유료 소모성 이용권"),
    FREE_CONSUMABLE("무료 소모성 이용권");

    private String description;

    PassType(String description) {
        this.description = description;
    }
}


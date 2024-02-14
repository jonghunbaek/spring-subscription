package com.example.subscription.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum PassType {
    SUBSCRIPTION("기간제 이용권"),
    CHAT_CONSUMABLE("소모성 질문권"),
    UNIT_CONSUMABLE("소모성 단원 학습권");

    private String description;

    PassType(String description) {
        this.description = description;
    }
}


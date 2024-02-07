package com.example.subscription.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Getter
@NoArgsConstructor
@Entity
public class SubscriptionProduct {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Long amount;

    private Long period;

    private Integer chatTimes;

    private Integer unitStudyTimes;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;
}

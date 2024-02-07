package com.example.subscription.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Getter
@NoArgsConstructor
@Entity
public class Subscription {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDateTime startDt;

    private LocalDateTime endDt;

    private int questionCount;

    private int unitStudyCount;

    @ManyToOne
    private SubscriptionProduct subscriptionProduct;

    @ManyToOne
    private Member member;

    @Builder
    private Subscription(LocalDateTime startDt, LocalDateTime endDt, int questionCount, int unitStudyCount, SubscriptionProduct subscriptionProduct, Member member) {
        this.startDt = startDt;
        this.endDt = endDt;
        this.questionCount = questionCount;
        this.unitStudyCount = unitStudyCount;
        this.subscriptionProduct = subscriptionProduct;
        this.member = member;
    }
}

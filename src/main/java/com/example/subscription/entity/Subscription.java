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

    // TODO :: 사용중, 미사용, 사용완료로 구분 
    private boolean isActive = true;

    @ManyToOne
    private SubscriptionProduct subscriptionProduct;

    @ManyToOne
    private Member member;

    @Builder
    private Subscription(LocalDateTime startDt, LocalDateTime endDt, int questionCount, int unitStudyCount, boolean isActive, SubscriptionProduct subscriptionProduct, Member member) {
        this.startDt = startDt;
        this.endDt = endDt;
        this.questionCount = questionCount;
        this.unitStudyCount = unitStudyCount;
        this.isActive = isActive;
        this.subscriptionProduct = subscriptionProduct;
        this.member = member;
    }

    public boolean isPeriodSubscription() {
        return SubscriptionType.PERIOD.equals(subscriptionProduct.getSubscriptionType()) && isActive;
    }

    public boolean isPaidSingleSubscription() {
        return SubscriptionType.PAID_SINGLE.equals(subscriptionProduct.getSubscriptionType()) && isActive;
    }

    public boolean isFreeSingleSubscription() {
        return SubscriptionType.FREE_SINGLE.equals(subscriptionProduct.getSubscriptionType()) && isActive;
    }
}

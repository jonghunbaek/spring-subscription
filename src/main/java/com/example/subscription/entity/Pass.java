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
public class Pass {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDateTime startDt;

    private LocalDateTime endDt;

    private int questionCount;

    private int unitStudyCount;

    // TODO :: 사용중, 미사용, 사용완료로 구분 
    private boolean isActive = true;

    @ManyToOne
    private PassProduct passProduct;

    @ManyToOne
    private Member member;

    @Builder
    private Pass(LocalDateTime startDt, LocalDateTime endDt, int questionCount, int unitStudyCount, boolean isActive, PassProduct passProduct, Member member) {
        this.startDt = startDt;
        this.endDt = endDt;
        this.questionCount = questionCount;
        this.unitStudyCount = unitStudyCount;
        this.isActive = isActive;
        this.passProduct = passProduct;
        this.member = member;
    }

    public boolean isPeriodSubscription() {
        return PassType.SUBSCRIPTION.equals(passProduct.getPassType()) && isActive;
    }

    public boolean isPaidSingleSubscription() {
        return PassType.PAID_CONSUMABLE.equals(passProduct.getPassType()) && isActive;
    }

    public boolean isFreeSingleSubscription() {
        return PassType.FREE_CONSUMABLE.equals(passProduct.getPassType()) && isActive;
    }
}

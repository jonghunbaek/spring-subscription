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

    private int remainingChatTimes;

    private int remainingUnitStudyTimes;

    private boolean isActive = true;

    @ManyToOne
    private PassProduct passProduct;

    @ManyToOne
    private Member member;

    @Builder
    private Pass(LocalDateTime startDt, LocalDateTime endDt, int remainingChatTimes, int remainingUnitStudyTimes, PassProduct passProduct, Member member) {
        this.startDt = startDt;
        this.endDt = endDt;
        this.remainingChatTimes = remainingChatTimes;
        this.remainingUnitStudyTimes = remainingUnitStudyTimes;
        this.passProduct = passProduct;
        this.member = member;
    }

    public boolean isSubscription() {
        return PassType.SUBSCRIPTION.equals(passProduct.getPassType());
    }
}

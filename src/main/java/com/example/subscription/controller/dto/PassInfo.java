package com.example.subscription.controller.dto;

import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PassInfo {

    private PassType passType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int remainingChatTimes;

    private int remainingUnitTimes;

    @Builder
    private PassInfo(PassType passType, LocalDateTime startDate, LocalDateTime endDate, int remainingChatTimes, int remainingUnitTimes) {
        this.passType = passType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingChatTimes = remainingChatTimes;
        this.remainingUnitTimes = remainingUnitTimes;
    }

    public static PassInfo fromSubscription(Pass subscriptionPass) {
        return PassInfo.builder()
            .passType(subscriptionPass.getPassProduct().getPassType())
            .startDate(subscriptionPass.getStartDt())
            .endDate(subscriptionPass.getEndDt())
            .build();
    }

    public static PassInfo fromConsumable(Pass consumablePass, int totalChatTimes) {
        return PassInfo.builder()
            .passType(consumablePass.getPassProduct().getPassType())
            .remainingChatTimes(totalChatTimes)
            .build();
    }
}

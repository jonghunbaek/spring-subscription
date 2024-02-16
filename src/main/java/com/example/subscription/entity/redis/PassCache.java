package com.example.subscription.entity.redis;

import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@RedisHash(value = "subscription", timeToLive = 3600)
public class PassCache {

    @Id
    private Long memberId;

    private PassType passType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int remainingChatTimes;

    private int remainingUnitTimes;

    @Builder
    private PassCache(Long memberId, PassType passType, LocalDateTime startDate, LocalDateTime endDate, int remainingChatTimes, int remainingUnitTimes) {
        this.memberId = memberId;
        this.passType = passType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingChatTimes = remainingChatTimes;
        this.remainingUnitTimes = remainingUnitTimes;
    }

    public static PassCache fromSubscription(Pass subscriptionPass) {
        return PassCache.builder()
            .memberId(subscriptionPass.getMember().getId())
            .passType(subscriptionPass.getPassProduct().getPassType())
            .startDate(subscriptionPass.getStartDt())
            .endDate(subscriptionPass.getEndDt())
            .build();
    }

    public static PassCache fromConsumable(Pass consumablePass, int totalChatTimes) {
        return PassCache.builder()
            .memberId(consumablePass.getMember().getId())
            .passType(consumablePass.getPassProduct().getPassType())
            .remainingChatTimes(totalChatTimes)
            .build();
    }
}

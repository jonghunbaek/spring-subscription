package com.example.subscription.entity.redis;

import com.example.subscription.entity.PassType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@RedisHash(value = "subscription", timeToLive = 3600)
public class PassRedis {

    @Id
    private Long memberId;

    private PassType passType;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int remainingChatTimes;

    private int remainingUnitTimes;

    @Builder
    private PassRedis(Long memberId, PassType passType, LocalDateTime startDate, LocalDateTime endDate, int remainingChatTimes, int remainingUnitTimes) {
        this.memberId = memberId;
        this.passType = passType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingChatTimes = remainingChatTimes;
        this.remainingUnitTimes = remainingUnitTimes;
    }
}

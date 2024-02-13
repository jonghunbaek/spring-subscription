package com.example.subscription.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RedisHash("subscription")
public class MemberSubscription {

    @Id
    private Long memberId;

    private SubscriptionType subscriptionType;

    private LocalDateTime expirationDate;
    private int chatCount;
    private int unitStudyCount;
}

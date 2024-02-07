package com.example.subscription.service;

import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.SubscriptionType;
import com.example.subscription.repo.SubscriptionRepository;
import com.example.subscription.service.dto.SubscribeInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SubscriptionServiceTest {

    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    SubscriptionRepository subscriptionRepository;

    @DisplayName("구독권 정보를 전달받아 구독권을 생성한다.")
    @Test
    void createSubscription() {
        // given
        SubscribeInfo subscribeInfo = SubscribeInfo.builder()
            .memberId(1L)
            .subscriptionProductId(1L)
            .quantity(1)
            .build();

        // when
        subscriptionService.createSubscription(subscribeInfo);
        Subscription subscription = subscriptionRepository.findAll().get(0);

        // then
        assertThat(subscription.getSubscriptionProduct().getSubscriptionType()).isEqualTo(SubscriptionType.PERIOD);
    }
}
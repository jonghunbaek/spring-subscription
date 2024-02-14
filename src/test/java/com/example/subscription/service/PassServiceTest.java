package com.example.subscription.service;

import com.example.subscription.controller.dto.PassInfo;
import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassType;
import com.example.subscription.repo.PassRepository;
import com.example.subscription.repo.redis.PassCacheRepository;
import com.example.subscription.service.dto.PurchaseInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class PassServiceTest {

    @Autowired
    PassService passService;
    @Autowired
    PassRepository passRepository;
    @Autowired
    PassCacheRepository passCacheRepository;

    @DisplayName("구독권 정보를 전달받아 구독권을 생성한다.")
    @Test
    void createSubscription() {
        // given
        PurchaseInfo purchaseInfo = createPurchaseInfo(1L);

        // when
        passService.createSubscription(purchaseInfo);
        Pass pass = passRepository.findAll().get(0);

        // then
        assertThat(pass.getPassProduct().getPassType()).isEqualTo(PassType.SUBSCRIPTION);
    }

    @DisplayName("구독성 이용권과 소모성 이용권 모두 존재하면 구독성 이용권 정보를 우선시 한다.")
    @Test
    void findSubscriptionPass() {
        // given
        PurchaseInfo subscriptionPassPurchaseInfo = createPurchaseInfo(1L);
        PurchaseInfo consumablePassPurchaseInfo = createPurchaseInfo(2L);
        passService.createSubscription(subscriptionPassPurchaseInfo);
        passService.createSubscription(consumablePassPurchaseInfo);

        // when
        PassInfo passInfo = passService.findSubscription(1L);

        // then
        assertThat(passInfo.getPassType()).isEqualTo(PassType.SUBSCRIPTION);
    }

    private static PurchaseInfo createPurchaseInfo(Long passProductId) {
        return PurchaseInfo.builder()
                .memberId(1L)
                .passProductId(passProductId)
                .quantity(1)
                .build();
    }
}
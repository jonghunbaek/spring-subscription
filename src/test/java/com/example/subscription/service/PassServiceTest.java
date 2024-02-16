package com.example.subscription.service;

import com.example.subscription.controller.dto.PassInfo;
import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassType;
import com.example.subscription.entity.redis.PassCache;
import com.example.subscription.repo.PassRepository;
import com.example.subscription.repo.redis.PassCacheRepository;
import com.example.subscription.service.dto.PurchaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
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
        PassInfo passInfo = passService.findPass(1L);

        // then
        assertThat(passInfo.getPassType()).isEqualTo(PassType.SUBSCRIPTION);
    }

    @DisplayName("구독성 이용권이 존재하면 Redis에 해당 이용권 정보를 담는다.")
    @Test
    void saveToRedis() {
        // given
        PurchaseInfo subscriptionPassPurchaseInfo = createPurchaseInfo(1L);
        passService.createSubscription(subscriptionPassPurchaseInfo);
        passService.findPass(1L);

        // when
        PassCache passCache = passCacheRepository.findById(1L)
            .orElseThrow(() -> new IllegalArgumentException("존재하는 이용권이 없습니다."));

        // then
        log.info("pass info :: {}", passCache);
        assertThat(passCache.getPassType()).isEqualTo(PassType.SUBSCRIPTION);
    }

    @DisplayName("구독성 이용권이 없고 소모성 이용권이 존재하면 Redis에 해당 이용권 정보를 담는다.")
    @Test
    void saveToRedisConsumable() {
        // given
        PurchaseInfo consumablePassPurchaseInfo = createPurchaseInfo(2L);
        passService.createSubscription(consumablePassPurchaseInfo);
        passService.findPass(1L);

        // when
        PassCache passCache = passCacheRepository.findById(1L)
            .orElseThrow(() -> new IllegalArgumentException("존재하는 이용권이 없습니다."));

        // then
        log.info("pass info :: {}", passCache);
        assertThat(passCache.getPassType()).isEqualTo(PassType.CHAT_CONSUMABLE);
        assertThat(passCache.getRemainingChatTimes()).isEqualTo(5);
    }

    @DisplayName("소모성 이용권에 남아있는 모든 질문권 수를 계산한다.")
    @Test
    void checkTotalChatTimes() {
        // given
        PurchaseInfo consumablePassPurchaseInfo = createPurchaseInfo(2L);
        passService.createSubscription(consumablePassPurchaseInfo);
        passService.createSubscription(consumablePassPurchaseInfo);
        passService.createSubscription(consumablePassPurchaseInfo);
        passService.findPass(1L);

        // when
        PassCache passCache = passCacheRepository.findById(1L)
            .orElseThrow(() -> new IllegalArgumentException("존재하는 이용권이 없습니다."));

        // then
        log.info("pass info :: {}", passCache);
        assertThat(passCache.getPassType()).isEqualTo(PassType.CHAT_CONSUMABLE);
        assertThat(passCache.getRemainingChatTimes()).isEqualTo(15);
    }

    @DisplayName("유효한 이용권이 없으면 예외를 던진다.")
    @Test
    void notExistPass() {
        assertThatThrownBy(() -> passService.findPass(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 사용자는 유효한 이용권을 가지고 있지 않습니다.");
    }

    private static PurchaseInfo createPurchaseInfo(Long passProductId) {
        return PurchaseInfo.builder()
                .memberId(1L)
                .passProductId(passProductId)
                .quantity(1)
                .build();
    }
}
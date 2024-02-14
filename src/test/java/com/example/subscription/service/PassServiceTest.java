package com.example.subscription.service;

import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassType;
import com.example.subscription.repo.PassRepository;
import com.example.subscription.service.dto.PurchaseInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PassServiceTest {

    @Autowired
    PassService passService;
    @Autowired
    PassRepository passRepository;

    @DisplayName("구독권 정보를 전달받아 구독권을 생성한다.")
    @Test
    void createSubscription() {
        // given
        PurchaseInfo purchaseInfo = PurchaseInfo.builder()
            .memberId(1L)
            .subscriptionProductId(1L)
            .quantity(1)
            .build();

        // when
        passService.createSubscription(purchaseInfo);
        Pass pass = passRepository.findAll().get(0);

        // then
        assertThat(pass.getPassProduct().getPassType()).isEqualTo(PassType.SUBSCRIPTION);
    }

    @DisplayName("사용자의 id를 인자로 받아 구독권 및 질문권 정보를 반환한다.")
    @Test
    void test() {
        // given

        // when

        // then

    }
}
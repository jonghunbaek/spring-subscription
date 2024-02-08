package com.example.subscription.repo;

import com.example.subscription.entity.Member;
import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.SubscriptionProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class SubscriptionRepositoryTest {

    @Autowired
    SubscriptionRepository subscriptionRepository;
    @Autowired
    SubscriptionProductRepository subscriptionProductRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("사용자 id를 인자로 가지고 있는 구독권을 조회한다.")
    @Test
    void findByMemberId() {
        // given
        SubscriptionProduct subscriptionProduct = subscriptionProductRepository.findById(1L).get();
        Member member = memberRepository.findById(1L).get();
        subscriptionRepository.save(Subscription.builder()
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusDays(subscriptionProduct.getPeriod()))
                .subscriptionProduct(subscriptionProduct)
                .member(member)
            .build());

        // when
        Subscription subscription = subscriptionRepository.findAllByMemberId(1L).get(0);

        // then
        assertThat(subscription.getSubscriptionProduct().getAmount()).isEqualTo(55000L);
    }

}
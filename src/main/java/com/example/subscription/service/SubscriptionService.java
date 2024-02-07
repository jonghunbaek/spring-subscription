package com.example.subscription.service;

import com.example.subscription.entity.Member;
import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.SubscriptionProduct;
import com.example.subscription.entity.SubscriptionType;
import com.example.subscription.repo.MemberRepository;
import com.example.subscription.repo.SubscriptionProductRepository;
import com.example.subscription.repo.SubscriptionRepository;
import com.example.subscription.service.dto.SubscribeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Transactional
@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionProductRepository subscriptionProductRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;

    public void createSubscription(SubscribeInfo subscribeInfo) {
        SubscriptionProduct subscriptionProduct = subscriptionProductRepository.findById(subscribeInfo.getSubscriptionProductId())
            .orElseThrow(() -> new IllegalArgumentException("일치하는 구독권이 없습니다."));

        Member member = memberRepository.findById(subscribeInfo.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));

        if (SubscriptionType.PERIOD.equals(subscriptionProduct.getSubscriptionType())) {
            savePeriodSubscription(subscriptionProduct, member);
            return;
        }

        saveSingleSubscriptions(subscribeInfo, subscriptionProduct, member);
    }

    private void savePeriodSubscription(SubscriptionProduct subscriptionProduct, Member member) {
        Subscription subscription;
        LocalDateTime startDt = LocalDateTime.now();

        subscription = Subscription.builder()
            .subscriptionProduct(subscriptionProduct)
            .startDt(startDt)
            .endDt(startDt.plusDays(subscriptionProduct.getPeriod()))
            .member(member)
            .build();

        subscriptionRepository.save(subscription);
    }

    private void saveSingleSubscriptions(SubscribeInfo subscribeInfo, SubscriptionProduct subscriptionProduct, Member member) {
        List<Subscription> subscriptions = IntStream.range(0, subscribeInfo.getQuantity())
            .mapToObj(i -> Subscription.builder()
                .subscriptionProduct(subscriptionProduct)
                .member(member)
                .build())
            .toList();

        subscriptionRepository.saveAll(subscriptions);
    }
}

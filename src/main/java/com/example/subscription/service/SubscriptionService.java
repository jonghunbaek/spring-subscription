package com.example.subscription.service;

import com.example.subscription.controller.dto.SubscriptionInfo;
import com.example.subscription.entity.Member;
import com.example.subscription.entity.Subscription;
import com.example.subscription.entity.SubscriptionProduct;
import com.example.subscription.entity.SubscriptionType;
import com.example.subscription.repo.MemberRepository;
import com.example.subscription.repo.SubscriptionProductRepository;
import com.example.subscription.repo.SubscriptionRepository;
import com.example.subscription.service.dto.PurchaseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Transactional
@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionProductRepository subscriptionProductRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;

    public String findSubscription(Long memberId) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByMemberId(memberId);

        Optional<Subscription> periodSub = subscriptions.stream()
            .filter(Subscription::isPeriodSubscription)
            .findAny();

        if (periodSub.isPresent()) {
            return "기간제 구독권 존재";
        }

        Optional<Subscription> paidSub = subscriptions.stream()
            .filter(Subscription::isPaidSingleSubscription)
            .findAny();

        if (paidSub.isPresent()) {
            return "유료 단건 구독권 존재";
        }

        Optional<Subscription> freeSub = subscriptions.stream()
            .filter(Subscription::isFreeSingleSubscription)
            .findAny();

        if (freeSub.isPresent()) {
            return "무료 단건 구독권 존재";
        }

        return "존재하는 구독권 없음";
    }

    public void createSubscription(PurchaseInfo purchaseInfo) {
        SubscriptionProduct subscriptionProduct = subscriptionProductRepository.findById(purchaseInfo.getSubscriptionProductId())
            .orElseThrow(() -> new IllegalArgumentException("일치하는 구독권이 없습니다."));

        Member member = memberRepository.findById(purchaseInfo.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));

        if (SubscriptionType.PERIOD.equals(subscriptionProduct.getSubscriptionType())) {
            savePeriodSubscription(subscriptionProduct, member);
            return;
        }

        saveSingleSubscriptions(purchaseInfo, subscriptionProduct, member);
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

    private void saveSingleSubscriptions(PurchaseInfo purchaseInfo, SubscriptionProduct subscriptionProduct, Member member) {
        List<Subscription> subscriptions = IntStream.range(0, purchaseInfo.getQuantity())
            .mapToObj(i -> Subscription.builder()
                .subscriptionProduct(subscriptionProduct)
                .member(member)
                .build())
            .toList();

        subscriptionRepository.saveAll(subscriptions);
    }
}

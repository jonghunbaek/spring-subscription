package com.example.subscription.service;

import com.example.subscription.entity.Member;
import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassProduct;
import com.example.subscription.entity.PassType;
import com.example.subscription.repo.MemberRepository;
import com.example.subscription.repo.PassProductRepository;
import com.example.subscription.repo.PassRepository;
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
public class PassService {

    private final PassProductRepository passProductRepository;
    private final PassRepository passRepository;
    private final MemberRepository memberRepository;

    public String findSubscription(Long memberId) {
        List<Pass> passes = passRepository.findAllByMemberId(memberId);

        Optional<Pass> periodSub = passes.stream()
            .filter(Pass::isPeriodSubscription)
            .findAny();

        if (periodSub.isPresent()) {
            return "기간제 구독권 존재";
        }

        Optional<Pass> paidSub = passes.stream()
            .filter(Pass::isPaidSingleSubscription)
            .findAny();

        if (paidSub.isPresent()) {
            return "유료 단건 구독권 존재";
        }

        Optional<Pass> freeSub = passes.stream()
            .filter(Pass::isFreeSingleSubscription)
            .findAny();

        if (freeSub.isPresent()) {
            return "무료 단건 구독권 존재";
        }

        return "존재하는 구독권 없음";
    }

    public void createSubscription(PurchaseInfo purchaseInfo) {
        PassProduct passProduct = passProductRepository.findById(purchaseInfo.getSubscriptionProductId())
            .orElseThrow(() -> new IllegalArgumentException("일치하는 구독권이 없습니다."));

        Member member = memberRepository.findById(purchaseInfo.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("일치하는 사용자가 없습니다."));

        if (PassType.SUBSCRIPTION.equals(passProduct.getPassType())) {
            savePeriodSubscription(passProduct, member);
            return;
        }

        saveSingleSubscriptions(purchaseInfo, passProduct, member);
    }

    private void savePeriodSubscription(PassProduct passProduct, Member member) {
        Pass pass;
        LocalDateTime startDt = LocalDateTime.now();

        pass = Pass.builder()
            .passProduct(passProduct)
            .startDt(startDt)
            .endDt(startDt.plusDays(passProduct.getPeriod()))
            .member(member)
            .build();

        passRepository.save(pass);
    }

    private void saveSingleSubscriptions(PurchaseInfo purchaseInfo, PassProduct passProduct, Member member) {
        List<Pass> passes = IntStream.range(0, purchaseInfo.getQuantity())
            .mapToObj(i -> Pass.builder()
                .passProduct(passProduct)
                .member(member)
                .build())
            .toList();

        passRepository.saveAll(passes);
    }
}

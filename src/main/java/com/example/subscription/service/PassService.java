package com.example.subscription.service;

import com.example.subscription.controller.dto.PassInfo;
import com.example.subscription.entity.Member;
import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassProduct;
import com.example.subscription.entity.PassType;
import com.example.subscription.entity.redis.PassRedis;
import com.example.subscription.repo.MemberRepository;
import com.example.subscription.repo.PassProductRepository;
import com.example.subscription.repo.PassRepository;
import com.example.subscription.repo.redis.PassRedisRepository;
import com.example.subscription.service.dto.PurchaseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@RequiredArgsConstructor
@Service
public class PassService {

    private final PassProductRepository passProductRepository;
    private final PassRepository passRepository;
    private final PassRedisRepository passRedisRepository;
    private final MemberRepository memberRepository;

    public PassInfo findSubscription(Long memberId) {
        Map<Boolean, List<Pass>> passGroup = findPassByType(memberId);
        List<Pass> subscriptionPass = passGroup.get(true);
        List<Pass> consumablePass = passGroup.get(false);

        // 기간제 이용권이 있을 경우
        if (isPresent(subscriptionPass)) {
            Pass pass = subscriptionPass.get(0);
            passRedisRepository.save(PassRedis.fromSubscription(pass));

            return PassInfo.fromSubscription(pass);
        }

        // 소모성 이용권이 있을 경우
        if (isPresent(consumablePass)) {
            int totalChatTimes = calculateTotalChatTimes(consumablePass);
            Pass pass = consumablePass.get(0);
            passRedisRepository.save(PassRedis.fromConsumable(pass, totalChatTimes));

            return PassInfo.fromConsumable(pass, totalChatTimes);
        }

        // 이용권이 없는 경우
        throw new IllegalArgumentException("해당 사용자는 유효한 이용권을 가지고 있지 않습니다.");
    }

    private Map<Boolean, List<Pass>> findPassByType(Long memberId) {
        List<Pass> passes = passRepository.findAllByMemberIdAndActiveIsTrue(memberId);

        return passes.stream()
            .collect(Collectors.groupingBy(Pass::isSubscription));
    }

    private static boolean isPresent(List<Pass> pass) {
        return !pass.isEmpty();
    }

    private int calculateTotalChatTimes(List<Pass> consumablePass) {
        return consumablePass.stream()
            .mapToInt(Pass::getRemainingChatTimes)
            .sum();
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

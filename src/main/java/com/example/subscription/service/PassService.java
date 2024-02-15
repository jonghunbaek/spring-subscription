package com.example.subscription.service;

import com.example.subscription.controller.dto.PassInfo;
import com.example.subscription.entity.Member;
import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassProduct;
import com.example.subscription.entity.PassType;
import com.example.subscription.entity.redis.PassCache;
import com.example.subscription.repo.MemberRepository;
import com.example.subscription.repo.PassProductRepository;
import com.example.subscription.repo.PassRepository;
import com.example.subscription.repo.redis.PassCacheRepository;
import com.example.subscription.service.dto.PurchaseInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class PassService {

    private final PassProductRepository passProductRepository;
    private final PassRepository passRepository;
    private final PassCacheRepository passCacheRepository;
    private final MemberRepository memberRepository;

    public PassInfo findPass(Long memberId) {
        Map<Boolean, List<Pass>> passGroup = findPassByType(memberId);

        return createPassInfo(passGroup);
    }

    private PassInfo createPassInfo(Map<Boolean, List<Pass>> passGroup) {
        List<Pass> subscriptionPass = passGroup.get(true);
        List<Pass> consumablePass = passGroup.get(false);

        if (isPresent(subscriptionPass)) {
            Pass pass = subscriptionPass.get(0);
            passCacheRepository.save(PassCache.fromSubscription(pass));

            return PassInfo.fromSubscription(pass);
        }

        if (isPresent(consumablePass)) {
            int totalChatTimes = calculateTotalChatTimes(consumablePass);
            Pass pass = consumablePass.get(0);
            passCacheRepository.save(PassCache.fromConsumable(pass, totalChatTimes));

            return PassInfo.fromConsumable(pass, totalChatTimes);
        }

        throw new IllegalArgumentException("해당 사용자는 유효한 이용권을 가지고 있지 않습니다.");
    }

    private Map<Boolean, List<Pass>> findPassByType(Long memberId) {
        List<Pass> passes = passRepository.findAllByMemberIdAndActive(memberId);

        return passes.stream()
            .collect(Collectors.groupingBy(Pass::isSubscription));
    }

    private static boolean isPresent(List<Pass> pass) {
        return !CollectionUtils.isEmpty(pass);
    }

    private int calculateTotalChatTimes(List<Pass> consumablePass) {
        return consumablePass.stream()
            .mapToInt(Pass::getRemainingChatTimes)
            .sum();
    }

    public void createSubscription(PurchaseInfo purchaseInfo) {
        PassProduct passProduct = passProductRepository.findById(purchaseInfo.getPassProductId())
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
        LocalDateTime startDt = LocalDateTime.now();

        Pass pass = Pass.builder()
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

package com.example.subscription.controller;

import com.example.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/sign-in")
    public String signIn(Long memberId, String password) {
        // 로그인 인증 로직 및 토큰 발행
        String subscription = subscriptionService.findSubscription(memberId);

        // 쿠키에 구독권 정보를 담는 로직 추가
        return "로그인 완료";
    }
}

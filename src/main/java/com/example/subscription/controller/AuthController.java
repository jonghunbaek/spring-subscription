package com.example.subscription.controller;

import com.example.subscription.controller.dto.PassInfo;
import com.example.subscription.service.PassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final PassService passService;

    @PostMapping("/sign-in")
    public void signIn(Long memberId, String password) {
        // 로그인 인증 로직 및 토큰 발행

        PassInfo subscription = passService.findSubscription(memberId);

        // 헤더에 구독권 정보를 담는 로직 추가
    }
}

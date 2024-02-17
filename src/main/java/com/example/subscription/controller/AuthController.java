package com.example.subscription.controller;

import com.example.subscription.controller.dto.AuthInfo;
import com.example.subscription.controller.dto.PassInfo;
import com.example.subscription.entity.PassType;
import com.example.subscription.service.PassService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final PassService passService;

    @PostMapping("/sign-in")
    public void signIn(@RequestBody AuthInfo authInfo, HttpServletResponse response) {
        // 로그인 인증 로직 및 토큰 발행
        PassInfo passInfo = passService.findPass(authInfo.getMemberId());

        // 헤더에 구독권 정보를 담는 로직 추가
        if (PassType.SUBSCRIPTION.equals(passInfo.getPassType())) {
            response.addHeader(passInfo.getPassType().toString(), passInfo.getStartDate().toString() + ":" + passInfo.getEndDate().toString());
            return;
        }

        response.addHeader(passInfo.getPassType().toString(), passInfo.getRemainingChatTimes() + ":" + passInfo.getRemainingUnitTimes());
    }
}

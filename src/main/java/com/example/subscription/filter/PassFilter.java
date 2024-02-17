package com.example.subscription.filter;

import com.example.subscription.repo.redis.PassCacheRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class PassFilter extends OncePerRequestFilter {

    private final PassCacheRepository passCacheRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] subscriptionList = {"/service1", "/service2"};
        return Arrays.stream(subscriptionList).anyMatch(subUri -> request.getRequestURI().startsWith(subUri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        // 쿠키에서 구독권 보유 여부를 체크 하는 로직
        filterChain.doFilter(request, response);
    }
}

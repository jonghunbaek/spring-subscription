package com.example.subscription.filter;

import com.example.subscription.entity.PassType;
import com.example.subscription.entity.redis.PassCache;
import com.example.subscription.repo.redis.PassCacheRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

    // 레디스에서 변경된 데이터를 즉각 DB에 반영하려면 어떻게 해야할까?
    // 1. 필터에서 PassService를 사용 - 최악
    // 2. 호출 되는 api에서 작업 - 차악
    // 3. 필터에서 2개의 api 호출하도록 가능?
    // 4. 기타 다른 방법은???

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestUri = request.getRequestURI();
        log.info("uri :: {}", requestUri);
        String[] passList = {"/question", "/service1", "/service2"};

        return Arrays.stream(passList)
                .filter(requestUri::contains)
                .findAny()
                .isEmpty();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        PassCache passCache = getPassCache(request);
        String requestURI = request.getRequestURI();

        if (passCache.isSubscription()) {
            filterChain.doFilter(request, response);
            return;
        }

        validateUriAccess(requestURI);

        passCacheRepository.save(passCache.deductChatTimes());
        response.setHeader(PassType.CHAT_CONSUMABLE.toString(), passCache.getRemainingChatTimes() + ":" + passCache.getRemainingUnitTimes());
        filterChain.doFilter(request, response);
    }

    private PassCache getPassCache(HttpServletRequest request) {
        String memberId = request.getHeader("MemberID");

        return passCacheRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 이용권 캐싱이 만료됐습니다. 재요청 바랍니다."));
    }

    private void validateUriAccess(String requestURI) {
        if (requestURI.contains("/service1") || requestURI.contains("/service2")) {
            throw new IllegalStateException("허용되지 않은 접근입니다.");
        }
    }
}

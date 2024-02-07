package com.example.subscription.aop;

import com.example.subscription.repo.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class SubscriptionAop {

    private final SubscriptionRepository subscriptionRepository;

    @Around("@annotation(com.example.subscription.aop.SubscriptionChecker)")
    public Object subscriptionAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.getArgs();

        return joinPoint.proceed();
    }
}

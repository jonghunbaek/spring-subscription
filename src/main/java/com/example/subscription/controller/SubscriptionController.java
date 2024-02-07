package com.example.subscription.controller;

import com.example.subscription.service.SubscriptionService;
import com.example.subscription.service.dto.SubscribeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public void subscribe(SubscribeInfo subscribeInfo) {
        subscriptionService.createSubscription(subscribeInfo);
    }
}

package com.example.subscription.controller;

import com.example.subscription.service.SubscriptionService;
import com.example.subscription.service.dto.PurchaseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody PurchaseInfo purchaseInfo) {
        subscriptionService.createSubscription(purchaseInfo);
    }
}

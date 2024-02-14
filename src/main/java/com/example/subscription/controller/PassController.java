package com.example.subscription.controller;

import com.example.subscription.service.PassService;
import com.example.subscription.service.dto.PurchaseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PassController {

    private final PassService passService;

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody PurchaseInfo purchaseInfo) {
        passService.createSubscription(purchaseInfo);
    }
}

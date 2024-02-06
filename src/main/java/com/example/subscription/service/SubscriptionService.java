package com.example.subscription.service;

import com.example.subscription.repo.SubscriptionProductRepository;
import com.example.subscription.repo.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionProductRepository subscriptionProductRepository;
    private final SubscriptionRepository subscriptionRepository;
}

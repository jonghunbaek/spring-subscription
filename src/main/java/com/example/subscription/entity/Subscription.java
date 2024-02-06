package com.example.subscription.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
public class Subscription {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private int questionCount;
    private int unitStudyCount;

    @ManyToOne
    private SubscriptionProduct subscriptionProduct;

    @ManyToOne
    private Member member;
}

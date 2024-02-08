package com.example.subscription.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MainService {
    public void serviceOne() {
        log.info("service1 실행");
    }

    public void serviceTwo() {
        log.info("service2 실행");
    }

    public void serviceThree() {
        log.info("service3 실행");
    }
}

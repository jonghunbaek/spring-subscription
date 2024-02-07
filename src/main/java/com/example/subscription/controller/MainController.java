package com.example.subscription.controller;

import com.example.subscription.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainController {

    private final MainService mainService;

    @GetMapping("/service1")
    public String apiOne() {
        mainService.serviceOne();
        return "1 API 응답";
    }

    @GetMapping("/service2")
    public String apiTwo() {
        mainService.serviceTwo();
        return "2 API 응답";
    }
}

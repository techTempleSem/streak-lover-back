package com.example.streak.health.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/open-api/health")
public class HealthController {

    @Value("${health}")
    String health;

    @GetMapping("")
    public String health(){
        log.info(health);
        return health;
    }
}

package com.example.streak.health.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/open-api/health")
public class HealthController {

    @GetMapping("")
    public String health(){
        log.info("hello");
        return "OK!!";
    }
}

package com.example.streak.health.controller;

import com.example.streak.common.api.Api;
import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.error.ErrorCodeIfs;
import com.example.streak.common.exception.ApiException;
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
    public Api<String> health(){
        log.info(health);
        return Api.OK(health);
    }
}

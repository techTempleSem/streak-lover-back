package com.example.streak.email.controller;

import com.example.streak.email.db.EmailAuthEntity;
import com.example.streak.email.model.EmailAuthRequest;
import com.example.streak.email.model.EmailInquiryRequest;
import com.example.streak.email.model.EmailRequest;
import com.example.streak.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/mail")
@RequiredArgsConstructor
@Slf4j
public class EmailOpenApiController {
    private final EmailService emailService;
    @PostMapping("/register")
    public String auth(
            @Valid
            @RequestBody
            EmailRequest emailRequest
    ){
        if(!emailService.checkValid(emailRequest.getEmail())){
            return "이미 있는 계정입니다.";
        }
        emailService.sendMail(emailRequest);
        return "";
    }

    @PostMapping("/auth")
    public String auth(
            @Valid
            @RequestBody EmailAuthRequest emailAuthRequest
    ) {
        return emailService.auth(emailAuthRequest);
    }

    @GetMapping("/all")
    public List<EmailAuthEntity> auth(
    ) {
//        return emailService.findAll();
        return null;
    }

    @GetMapping("/clear")
    public String clearDummy(
    ) {
//        return emailService.clearDummy();
        return null;
    }
}

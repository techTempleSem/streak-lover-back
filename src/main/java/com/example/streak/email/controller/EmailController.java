package com.example.streak.email.controller;

import com.example.streak.email.db.EmailAuthEntity;
import com.example.streak.email.model.EmailAuthRequest;
import com.example.streak.email.model.EmailRequest;
import com.example.streak.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/mail")
@RequiredArgsConstructor
public class EmailController {
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
        return emailService.findAll();
    }

    @GetMapping("/clear")
    public String clearDummy(
    ) {
        return emailService.clearDummy();
    }
}

package com.example.streak.email.controller;

import com.example.streak.email.db.EmailRequest;
import com.example.streak.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/mail")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/hello")
    public String auth(
            @Valid
            @RequestBody
            EmailRequest emailRequest
    ){
        emailService.sendMail(emailRequest);
        return "";
    }
}

package com.example.streak.email.controller;

import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.email.db.EmailAuthEntity;
import com.example.streak.email.model.EmailAuthRequest;
import com.example.streak.email.model.EmailInquiryRequest;
import com.example.streak.email.model.EmailRequest;
import com.example.streak.email.service.EmailService;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@Slf4j
public class EmailApiController {
    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping("/inquiry")
    public String inquiry(
            @Valid
            @RequestBody EmailInquiryRequest emailInquiryRequest,
            HttpSession httpSession
    ) {

        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> _userEntity = userRepository.findById(id);
        if(_userEntity.isEmpty()) throw new ApiException(ErrorCode.UNAUTHORIZED);
        UserEntity userEntity = _userEntity.get();

        EmailRequest request = EmailRequest.builder()
                .email("techtemplesem@gmail.com")
                .type("inquiry")
                .title(userEntity.getName() + " : " + emailInquiryRequest.getTitle())
                .context(emailInquiryRequest.getContext()).build();
        log.info("{}",request);
        emailService.sendMail(request);
        return "OK!";
    }
}

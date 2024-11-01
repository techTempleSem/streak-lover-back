package com.example.streak.email.service;

import com.example.streak.email.db.EmailAuthEntity;
import com.example.streak.email.db.EmailRepository;
import com.example.streak.email.model.EmailAuthRequest;
import com.example.streak.email.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final EmailRepository emailRepository;

    public void sendMail(
            EmailRequest emailRequest
    ){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String code = createCode();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailRequest.getEmail());
            mimeMessageHelper.setSubject("스트릭 서비스 회원가입 메일입니다");
            mimeMessageHelper.setText(setContext(emailRequest.getType(), code), true);
            javaMailSender.send(mimeMessage);

            log.info("===");
            log.info(emailRequest.getType());
            log.info(code);

            if(Objects.equals(emailRequest.getType(), "email")){
                emailRepository.save(emailRequest.getEmail(), code);
            }

            log.info("Success");

        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            key.append((char) ((int) random.nextInt(26) + 65));
        }
        return key.toString();
    }

    public String setContext(String type, String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return springTemplateEngine.process(type, context);
    }

    public List<EmailAuthEntity> findAll() {
        return emailRepository.findAll();
    }

    public String clearDummy() {
        return emailRepository.clearDummy();
    }

    public String auth(@Valid EmailAuthRequest emailAuthRequest) {
        Optional<EmailAuthEntity> _email = emailRepository.findByEmail(emailAuthRequest.getEmail());
        if(_email.isEmpty()) {
            return "인증코드가 전송되지 않았습니다.";
        }
        EmailAuthEntity email = _email.get();
        if(email.getIsAuth()){
            return "이미 인증되었습니다.";
        }
        if(email.getAuthAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            return "인증 시간이 초과되었습니다. 다시 인증해주세요.";
        }
        if(!Objects.equals(email.getCode(), emailAuthRequest.getCode())) {
            return "인증 코드가 틀렸습니다. 다시 인증해 주세요.";
        }
        email.setIsAuth(Boolean.TRUE);
        return "성공!";
    }
}

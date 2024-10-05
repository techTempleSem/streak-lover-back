package com.example.streak.email.service;

import com.example.streak.email.db.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    public void sendMail(
            EmailRequest emailRequest
    ){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailRequest.getEmail());
            mimeMessageHelper.setSubject("HELLO!!");
            mimeMessageHelper.setText(setContext(emailRequest.getEmail()), true);
            javaMailSender.send(mimeMessage);

            log.info("Success");

        } catch (MessagingException e) {
            log.info("fail");
            throw new RuntimeException(e);
        }
    }

    public String setContext(String email) {
        Context context = new Context();
        context.setVariable("who", email);
        return springTemplateEngine.process("hello", context);
    }
}

package com.example.streak.email.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailRepository {
    private List<EmailAuthEntity> emailAuthEntityList = new ArrayList<>();

    public Optional<EmailAuthEntity> findByEmail(
            String email
    ){
        return emailAuthEntityList.stream().filter(
                it -> Objects.equals(it.getEmail(), email)
        ).findFirst();
    }

    public void delete(
            String email
    ){
        EmailAuthEntity emailAuthEntity = findByEmail(email).get();
        emailAuthEntityList.remove(emailAuthEntity);
    }

    public List<EmailAuthEntity> findAll(){
        return emailAuthEntityList;
    }

    public void save(String email, String code){
        Optional<EmailAuthEntity> alreadyRegister = findByEmail(email);
        alreadyRegister.ifPresent(emailAuthEntity -> emailAuthEntityList.remove(emailAuthEntity));
        EmailAuthEntity emailAuthEntity = EmailAuthEntity.builder()
                .email(email)
                .authAt(LocalDateTime.now())
                .code(code)
                .isAuth(false)
                .build();
        log.info("===");
        log.info(emailAuthEntity.getEmail());
        log.info(emailAuthEntity.getCode());
        emailAuthEntityList.add(emailAuthEntity);
    }

    public String clearDummy() {
        emailAuthEntityList = emailAuthEntityList.stream().filter(
                it -> it.getAuthAt().isAfter(LocalDateTime.now().minusMinutes(5))
        ).collect(Collectors.toList());
        return "SUCCESS!";
    }
}

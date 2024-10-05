package com.example.streak.user.controller;

import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.model.UserLoginRequest;
import com.example.streak.work.db.WorkEntity;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/open-api/user")
@RequiredArgsConstructor
public class UserOpenApiController {
    private final UserRepository userRepository;

    @PostMapping("/login")
    public String login(
            @Valid
            @RequestBody
            UserLoginRequest userLoginRequest,

            HttpSession httpSession
    ){
        log.info(String.valueOf(userLoginRequest));
        Optional<UserEntity> user = userRepository.findFirstByNameAndPassword(userLoginRequest.getName(),
                userLoginRequest.getPassword());

        if(user.isPresent()){
            httpSession.setAttribute("USER", user.get().getId());
            return "YES";
        } else {
            return "NO";
        }
    }
}

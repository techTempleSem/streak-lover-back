package com.example.streak.user.controller;

import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.model.UserLoginRequest;
import com.example.streak.user.model.UserRegisterRequest;
import com.example.streak.user.service.UserService;
import com.example.streak.work.db.WorkEntity;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.streak.utils.Encrypt.encrypt;

@Slf4j
@RestController
@RequestMapping("/open-api/user")
@RequiredArgsConstructor
public class UserOpenApiController {
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/login")
    public String login(
            @Valid
            @RequestBody
            UserLoginRequest userLoginRequest,

            HttpSession httpSession
    ){
        String encryptPassword = encrypt(userLoginRequest.getPassword());

        Optional<UserEntity> user = userRepository.findFirstByNameAndPassword(userLoginRequest.getName(),
                encryptPassword);

        if(user.isPresent()){
            httpSession.setAttribute("USER", user.get().getId());
            return "YES";
        } else {
            return "NO";
        }
    }

    @PostMapping("/register")
    public String register(
            @Valid
            @RequestBody
            UserRegisterRequest userRegisterRequest,

            HttpSession httpSession
    ){
        Optional<UserEntity> user = userRepository.findFirstByName(userRegisterRequest.getName());

        if(user.isPresent()){
            return "이미 있는 계정입니다.";
        } else {
            return userService.register(userRegisterRequest);
        }
    }
}

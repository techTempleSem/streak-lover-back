package com.example.streak.user.controller;

import com.example.streak.common.api.Api;
import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.firebase.db.FirebaseEntity;
import com.example.streak.firebase.db.FirebaseRepository;
import com.example.streak.firebase.service.FirebaseService;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.model.*;
import com.example.streak.user.service.UserConverter;
import com.example.streak.user.service.UserService;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.service.WorkConverter;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Transactional
public class UserApiController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserConverter userConverter;
    private final FirebaseService firebaseService;

    @GetMapping("/work")
    public List<WorkDTO> work(
            HttpSession httpSession
    ){
        Long id = (Long)(httpSession.getAttribute("USER"));
        return userService.getWorks(id);
    }

    @GetMapping("/logout")
    public void logout(
            HttpSession httpSession
    ){
        httpSession.removeAttribute("USER");
    }

    @GetMapping("/user")
    public UserDTO user(
            HttpSession httpSession
    ){
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> _userEntity = userRepository.findById(id);
        if(_userEntity.isEmpty()) throw new ApiException(ErrorCode.UNAUTHORIZED);
        UserEntity userEntity = _userEntity.get();
        return userConverter.toDTO(userEntity);
    }

    @PostMapping("/change")
    public Api<String> change(
            @Valid
            @RequestBody
            UserChangeRegister userChangeRegister,
            HttpSession httpSession
    ){
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> _userEntity = userRepository.findById(id);
        if(_userEntity.isEmpty()) throw new ApiException(ErrorCode.UNAUTHORIZED);
        UserEntity userEntity = _userEntity.get();

        return Api.OK(userService.change(userEntity, userChangeRegister));
    }

    @PostMapping("/firebase-token")
    public Api<String> token(
            @Valid
            @RequestBody
            UserTokenRequest userTokenRequest,
            HttpSession httpSession
    ){
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> _userEntity = userRepository.findById(id);
        if(_userEntity.isEmpty()) throw new ApiException(ErrorCode.UNAUTHORIZED);
        UserEntity userEntity = _userEntity.get();

        return firebaseService.save(userEntity, userTokenRequest);
    }

    @PostMapping("/alert-time")
    public Api<String> alertTime(
            @Valid
            @RequestBody
            UserAlertTimeRequest userAlertTimeRequest,
            HttpSession httpSession
    ){
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> _userEntity = userRepository.findById(id);
        if(_userEntity.isEmpty()) throw new ApiException(ErrorCode.UNAUTHORIZED);
        UserEntity userEntity = _userEntity.get();

        return userService.setAlertTime(userEntity, userAlertTimeRequest.getAlertTime());
    }
}

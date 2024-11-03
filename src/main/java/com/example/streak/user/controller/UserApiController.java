package com.example.streak.user.controller;

import com.example.streak.common.api.Api;
import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.model.UserDTO;
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
        if(_userEntity.isEmpty()) return null;
        UserEntity userEntity = _userEntity.get();
        return userConverter.toDTO(userEntity);
    }
}

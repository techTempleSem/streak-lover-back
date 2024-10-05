package com.example.streak.user.controller;

import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.work.db.WorkEntity;
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

    @GetMapping("/work")
    public List<WorkEntity> work(
            HttpSession httpSession
    ){
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()) return null;
        return userEntity.get().getWork();
    }
}

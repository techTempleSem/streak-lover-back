package com.example.streak.work.controller;

import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.work.model.WorkAddRequest;
import com.example.streak.work.service.WorkService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/work")
@RequiredArgsConstructor
public class WorkApiController {

    private final UserRepository userRepository;
    private final WorkService workService;

    @PostMapping("/add")
    private String add(
            @Valid
            @RequestBody
            WorkAddRequest workAddRequest,

            HttpSession httpSession
    ) throws Exception {
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()) return "로그인 해 주세요";

        workService.addStreak(id, workAddRequest.getOrder());

        return "스트릭 연장 성공";
    }
}

package com.example.streak.work.controller;

import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.model.WorkExtendRequest;
import com.example.streak.work.model.WorkRegisterRequest;
import com.example.streak.work.service.WorkService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/work")
@RequiredArgsConstructor
public class WorkApiController {

    private final UserRepository userRepository;
    private final WorkService workService;

    @PostMapping("/extend")
    private List<WorkDTO> extend(
            @Valid
            @RequestBody
            WorkExtendRequest workAddRequest,

            HttpSession httpSession
    ) throws Exception {
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()) return null;

        return workService.extendStreak(id, workAddRequest.getOrder());
    }

    @PostMapping("/register")
    private String register(
            @Valid
            @RequestBody
            WorkRegisterRequest workRegisterRequest,

            HttpSession httpSession
    ) throws Exception {
        log.info("=====");
        log.info(workRegisterRequest.getTitle());
        System.out.println(workRegisterRequest.getSelectedDays());
        log.info("=====");

        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()) return "등록이 실패하였습니다.";

        log.info("=====");
        log.info(workRegisterRequest.getTitle());
        log.info("=====");

        return workService.register(id, workRegisterRequest);
    }
}

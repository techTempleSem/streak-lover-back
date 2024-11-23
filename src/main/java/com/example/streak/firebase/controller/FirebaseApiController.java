package com.example.streak.firebase.controller;

import com.example.streak.common.api.Api;
import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.firebase.service.FirebaseService;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.model.WorkExtendRequest;
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
@RequestMapping("/api/firebase")
@RequiredArgsConstructor
public class FirebaseApiController {
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;

    @PostMapping("/alert")
    private Api<String> alert(
            HttpSession httpSession
    ) throws Exception {
        Long id = (Long)(httpSession.getAttribute("USER"));
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isEmpty()) throw new ApiException(ErrorCode.UNAUTHORIZED);

        firebaseService.alert("dggS7se47lJ3seCTV47yhh:APA91bHkXCrBkpydrDMViJ7MNOSLT3L-BG1_kaWY_x_DjVPuB6Q_DeVT17jPi4QB6ucYb6ExC6gE94nBmRJOJYq1U7tw59V4kWPuhSXyXWqvZLlQzZBHPfw","hello","world");
        return Api.OK("alert!");
    }
}

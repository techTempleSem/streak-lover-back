package com.example.streak.work.service;

import com.example.streak.streak.business.StreakBusiness;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.service.UserService;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.db.WorkRepository;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.model.WorkRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final StreakBusiness streakBusiness;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<WorkDTO> extendStreak(
            Long userId,
            Long workOrder
    ) throws Exception {
        Optional<WorkEntity> work = workRepository.findFirstByUserIdAndOrderNum(userId, workOrder);
        if(work.isEmpty()){
            throw new Exception("해당 작업이 없습니다.");
        }

        if(!streakBusiness.isValidExtend(work.get().getId())){
            throw new Exception("이미 완료했습니다.");
        }

        streakBusiness.extend(work.get().getId());

        return userService.getWorks(userId);
    }

    public String register(
            Long id,
            @Valid WorkRegisterRequest workRegisterRequest
    ) {
        int size = userService.getWorks(id).size();
        Optional<UserEntity> _user = userRepository.findById(id);
        if(_user.isEmpty()) return "로그인이 필요합니다";
        UserEntity user = _user.get();

        log.info("=====");
        log.info(String.valueOf(workRegisterRequest.getSelectedDays().size()));
        log.info("=====");

        int dayWeek = streakBusiness.weekToNumber(workRegisterRequest.getSelectedDays());

        WorkEntity workEntity = WorkEntity.builder()
                .name(workRegisterRequest.getTitle())
                .createdAt(LocalDateTime.now())
                .user(user)
                .orderNum(size + 1)
                .lastUpdatedAt(LocalDateTime.of(1970,1,1,0,0))
                .curStreak(0)
                .dayWeek(dayWeek).build();

        workRepository.save(workEntity);
        return  "성공!";
    }
}

package com.example.streak.work.service;

import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.streak.business.StreakBusiness;
import com.example.streak.streak.db.StreakEntity;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.service.UserService;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.db.WorkRepository;
import com.example.streak.work.db.enums.WorkState;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.model.WorkRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
        Optional<WorkEntity> work = workRepository.findById(workOrder);
        if(work.isEmpty()){
            throw new Exception("해당 작업이 없습니다.");
        }

        if(!Objects.equals(work.get().getUser().getId(), userId)){
            throw new Exception("해당 유저가 아닙니다.");
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

        int dayWeek = streakBusiness.weekToNumber(workRegisterRequest.getSelectedDays());

        if(dayWeek == 0) return "요일이 적어도 하나는 체크되어 있어야 합니다.";

        if(workRegisterRequest.getWorkNum() == null) log.info("null");
        else log.info("not null");

        if(workRegisterRequest.getWorkNum() != null){
            Optional<WorkEntity> _work = workRepository.findById(workRegisterRequest.getWorkNum());
            if(_work.isEmpty()) return "에러!";
            WorkEntity work = _work.get();
            work.setName(workRegisterRequest.getTitle());
            work.setDescript(workRegisterRequest.getDescription());
            work.setDayWeek(dayWeek);
            workRepository.save(work);
            return "성공!";
        }

        WorkEntity workEntity = WorkEntity.builder()
                .name(workRegisterRequest.getTitle())
                .descript(workRegisterRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .user(user)
                .orderNum(size + 1)
                .lastUpdatedAt(LocalDateTime.of(1970,1,1,0,0))
                .curStreak(0)
                .dayWeek(dayWeek)
                .state(WorkState.NORMAL).build();

        workRepository.save(workEntity);
        return "성공!";
    }

    public WorkEntity find(Long userId, Long streakId) {
        Optional<WorkEntity> _workEntity =workRepository.findById(streakId);
        if(_workEntity.isEmpty()){
            throw new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 작업입니다.");
        }
        WorkEntity workEntity = _workEntity.get();

        if(!Objects.equals(workEntity.getUser().getId(), userId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "올바른 유저가 아닙니다.");
        }
        Collections.sort(workEntity.getStreak(), new Comparator<StreakEntity>() {
            @Override
            public int compare(StreakEntity p1, StreakEntity p2) {
                return Integer.compare(p2.getMonth(), p1.getMonth());
            }
        });

        if(workEntity.getStreak().size() > 15){
            workEntity.setStreak(workEntity.getStreak().subList(0,15));
        }
        return workEntity;
    }

    public void delete(Long userId, Long streakId) {
        Optional<WorkEntity> _workEntity =workRepository.findById(streakId);
        if(_workEntity.isEmpty()){
            return;
        }
        WorkEntity workEntity = _workEntity.get();

        if(!Objects.equals(workEntity.getUser().getId(), userId)) {
            return;
        }
        workEntity.setState(WorkState.DELETE);
        workRepository.save(workEntity);
    }
}

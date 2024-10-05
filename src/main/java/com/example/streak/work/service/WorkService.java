package com.example.streak.work.service;

import com.example.streak.streak.business.StreakBusiness;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.model.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final StreakBusiness streakBusiness;

    public void addStreak(
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
    }
}

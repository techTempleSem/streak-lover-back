package com.example.streak.streak.business;

import com.example.streak.streak.db.StreakEntity;
import com.example.streak.streak.db.StreakRepository;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.model.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class StreakBusiness {
    private final StreakRepository streakRepository;
    private final WorkRepository workRepository;

    public boolean isValidExtend(Long workId){
        Optional<StreakEntity> _streak = streakRepository.findFirstByWorkIdOrderByMonthDesc(workId);
        if(_streak.isEmpty()) {
            return true;
        }
        StreakEntity streak = _streak.get();
        LocalDate now = LocalDate.now();
        int month = now.getYear() * 100 + now.getMonthValue();
        if(streak.getMonth() != month){
            return true;
        }
        int dayBit = (1 << (now.getDayOfMonth() - 1));
        if((streak.getCheckNum() & dayBit) == 0) {
            return true;
        }
        return false;
    }

    public void extend(Long workId){
        LocalDate now = LocalDate.now();
        int month = now.getYear() * 100 + now.getMonthValue();
        int dayBit = (1 << (now.getDayOfMonth() - 1));

        Optional<StreakEntity> _streak = streakRepository.findFirstByWorkIdOrderByMonthDesc(workId);
        if(_streak.isEmpty() || _streak.get().getMonth() != month) {
            WorkEntity work = workRepository.findById(workId).get();
            StreakEntity streak = StreakEntity.builder()
                    .checkNum(dayBit)
                    .month(month)
                    .work(work)
                    .build();
            streakRepository.save(streak);
            return;
        }
        StreakEntity streak = _streak.get();
        streak.setCheckNum(streak.getCheckNum() | dayBit);
        streakRepository.save(streak);
    }
}

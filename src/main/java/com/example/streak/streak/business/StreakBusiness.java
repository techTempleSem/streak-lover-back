package com.example.streak.streak.business;

import com.example.streak.streak.db.StreakEntity;
import com.example.streak.streak.db.StreakRepository;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.db.WorkRepository;
import com.example.streak.work.db.enums.WorkState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class StreakBusiness {
    private final StreakRepository streakRepository;
    private final WorkRepository workRepository;

    public static boolean isYesterday(LocalDateTime dateTime) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate targetDate = dateTime.toLocalDate();
        return yesterday.equals(targetDate);
    }

    public boolean isValidExtend(Long workId){
        log.info("id : {}",workId);
        LocalDate now = LocalDate.now();
        log.info("now : {}",now);
        WorkEntity work = workRepository.findById(workId).get();
        if(work.getState() == WorkState.DELETE){
            return false;
        }
        if((work.getDayWeek() & (1<<(now.getDayOfWeek().getValue() % 7)))==0){
            return false;
        }
        Optional<StreakEntity> _streak = streakRepository.findFirstByWorkIdOrderByMonthDesc(workId);
        List<StreakEntity> _test = streakRepository.findAllByWorkIdOrderByMonthDesc(workId);
        if(_streak.isEmpty()) {
            return true;
        }
        StreakEntity streak = _streak.get();
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

    public void update(WorkEntity work){
        LocalDateTime lastTime = work.getLastUpdatedAt();
        LocalDate lastDay = LocalDate.from(lastTime);
        int streakCount = work.getCurStreak();
        while(lastDay.isBefore(LocalDate.now().minusDays(1))){
            lastDay = lastDay.plusDays(1);
            int week = (lastDay.getDayOfWeek().getValue()) % 7;
            if((work.getDayWeek() & (1 << week)) != 0){
                 break;
            }
            lastTime = lastTime.plusDays(1);
            streakCount++;
        }
        work.setLastUpdatedAt(lastTime);
        work.setCurStreak(streakCount);
    }

    public void updateDate(WorkEntity work, LocalDate now){
        int month = now.getYear() * 100 + now.getMonthValue();
        int dayBit = (1 << (now.getDayOfMonth() - 1));

        Optional<StreakEntity> _streak = streakRepository.findFirstByWorkIdOrderByMonthDesc(work.getId());
        if(_streak.isEmpty() || _streak.get().getMonth() != month) {
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

    public void extend(Long workId){

        WorkEntity work = workRepository.findById(workId).get();
        LocalDateTime lastUpdatedAt = work.getLastUpdatedAt();
        update(work);
        if(isYesterday(work.getLastUpdatedAt())) {
            work.setCurStreak(work.getCurStreak() + 1);
        } else {
            work.setCurStreak(1);
        }
        work.setLastUpdatedAt(LocalDateTime.now());
        work.setMoney(work.getMoney() + 100);
        workRepository.save(work);

        updateDate(work, LocalDate.now());
    }

    public int weekToNumber(Map<String, Boolean> week){
        int num = 0;
        if(week.get("일")) num += 1;
        if(week.get("월")) num += 2;
        if(week.get("화")) num += 4;
        if(week.get("수")) num += 8;
        if(week.get("목")) num += 16;
        if(week.get("금")) num += 32;
        if(week.get("토")) num += 64;
        return num;
    }
}

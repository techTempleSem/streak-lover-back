package com.example.streak.utils;

import com.example.streak.common.api.Api;
import com.example.streak.firebase.service.FirebaseService;
import com.example.streak.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PushNotification {
    private final UserService userService;
    @Scheduled(cron = "0 0/30 * * * ?")  // 매일 9시 (21:00)
//    @Scheduled(fixedRate = 1000*60)
    public void schedulePushNotification() {
        try {
            userService.alert();
        } catch (Exception e) {
            System.err.println("푸시 알림 전송 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        log.info("alert!");
    }
}

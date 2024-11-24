package com.example.streak.utils;

import com.example.streak.common.api.Api;
import com.example.streak.firebase.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PushNotification {
    private final FirebaseService firebaseService;
    @Scheduled(cron = "0 0 21 * * ?")  // 매일 9시 (21:00)
    public void schedulePushNotification() {
        try {
            firebaseService.alert("dggS7se47lJ3seCTV47yhh:APA91bHkXCrBkpydrDMViJ7MNOSLT3L-BG1_kaWY_x_DjVPuB6Q_DeVT17jPi4QB6ucYb6ExC6gE94nBmRJOJYq1U7tw59V4kWPuhSXyXWqvZLlQzZBHPfw","hello","world");
        } catch (Exception e) {
            System.err.println("푸시 알림 전송 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        log.info("alert!");
    }
}

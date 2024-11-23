package com.example.streak.firebase.service;

import com.example.streak.common.api.Api;
import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.firebase.db.FirebaseEntity;
import com.example.streak.firebase.db.FirebaseRepository;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.model.UserTokenRequest;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseService {
    @Value("${FIREBASE_SERVER_KEY}")
    String FIREBASE_SERVER_KEY;
    @Value("${FIREBASE_API_URL}")
    String FIREBASE_API_URL;
    @Value("${firebase.config}")
    String firebaseConfigPath;

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    private final FirebaseRepository firebaseRepository;
    @Autowired
    private RestTemplate restTemplate;
    public Api<String> save(
            UserEntity userEntity,
            UserTokenRequest userTokenRequest
    ){
        List<FirebaseEntity> firebaseEntities = userEntity.getFirebase();
        for(FirebaseEntity firebase : firebaseEntities){
            if(Objects.equals(firebase.getToken(), userTokenRequest.getToken())){
                return Api.OK("이미 있는 토큰입니다");
            }
        }

        FirebaseEntity firebase = FirebaseEntity.builder()
                .token(userTokenRequest.getToken())
                .user(userEntity).build();
        firebaseRepository.save(firebase);
        return Api.OK("갱신 완료!");
    }

    private String getAccessToken() throws IOException {
        log.info(firebaseConfigPath);
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream(firebaseConfigPath))
                .createScoped(Arrays.asList(SCOPES));
        var token = googleCredentials.refreshAccessToken();
        log.info("{}",token);
        return token.getTokenValue();
    }

    public void alert(
            String targetToken, String title, String body
    ){
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);

        Map<String, Object> message = new HashMap<>();
        message.put("notification", notification);
        message.put("token", targetToken);

        Map<String, Object> messages = new HashMap<>();
        messages.put("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            String token = getAccessToken();
            log.info(token);
            headers.setBearerAuth(token);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ApiException(ErrorCode.UNAUTHORIZED, "firebase 오류");
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(messages, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(FIREBASE_API_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Notification sent successfully");
        } else {
            log.info("Failed to send notification");
        }
    }
}

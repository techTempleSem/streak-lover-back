package com.example.streak.user.service;

import com.example.streak.common.api.Api;
import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.email.db.EmailAuthEntity;
import com.example.streak.email.db.EmailRepository;
import com.example.streak.email.model.EmailRequest;
import com.example.streak.email.service.EmailService;
import com.example.streak.firebase.db.FirebaseEntity;
import com.example.streak.firebase.service.FirebaseService;
import com.example.streak.streak.business.StreakBusiness;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.db.enums.UserState;
import com.example.streak.user.model.UserChangeRegister;
import com.example.streak.user.model.UserPasswordRequest;
import com.example.streak.user.model.UserRegisterRequest;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.db.enums.WorkState;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.service.WorkConverter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.streak.utils.Encrypt.encrypt;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final WorkConverter workConverter;
    private final EmailRepository emailRepository;
    private final EmailService emailService;
    private final StreakBusiness streakBusiness;
    private final FirebaseService firebaseService;

    public String register(UserRegisterRequest userRegisterRequest){
        Optional<EmailAuthEntity> _emailAuthEntity = emailRepository.findByEmail(userRegisterRequest.getName());
        if(_emailAuthEntity.isEmpty() || !_emailAuthEntity.get().getIsAuth()) {
            return "인증을 진행해 주세요";
        }

        String encryptPassword = encrypt(userRegisterRequest.getPassword());
        UserEntity user = UserEntity.builder()
                .createdAt(LocalDateTime.now())
                .name(userRegisterRequest.getName())
                .password(encryptPassword)
                .alertTime("22:00")
                .state(UserState.NORMAL)
                .workCount(5).build();
        userRepository.save(user);
        return "성공적으로 등록되었습니다!";
    }

    public List<WorkDTO> getWorks(Long id){
        Optional<UserEntity> _userEntity = userRepository.findById(id);
        if(_userEntity.isEmpty()) return null;
        UserEntity userEntity = _userEntity.get();
        List<WorkEntity> works = userEntity.getWork();
        List<WorkDTO> workDTOs = works.stream().filter(work -> {
            return work.getState() != WorkState.DELETE;
        }).map(work -> workConverter.toDTO(work)).toList();
        return workDTOs;
    }

    public String change(
            UserEntity userEntity,
            UserChangeRegister userChangeRegister
    ){
        log.info(userChangeRegister.getNewPassword());
        log.info(userChangeRegister.getConfirmPassword());
        if(!Objects.equals(userChangeRegister.getNewPassword(), userChangeRegister.getConfirmPassword())){
            throw new ApiException(ErrorCode.BAD_REQUEST, "비밀번호 확인 탭을 다시 확인해 주세요");
        }
        String encryptPassword = encrypt(userChangeRegister.getCurrentPassword());
        if(!Objects.equals(userEntity.getPassword(), encryptPassword)){
            throw new ApiException(ErrorCode.BAD_REQUEST, "비밀번호를 다시 확인해 주세요");
        }
        String encryptNewPassword = encrypt(userChangeRegister.getNewPassword());
        userEntity.setPassword(encryptNewPassword);
        userRepository.save(userEntity);
        return "성공!";
    }

    public String password(
            @Valid UserPasswordRequest userPasswordRequest,
            UserEntity userEntity
    ) {
        EmailRequest emailRequest = EmailRequest.builder()
                .email(userPasswordRequest.getEmail())
                .type("password").build();
        emailService.sendMail(emailRequest);
        return "성공!";
    }

    @Transactional
    public void alert() {
        List<UserEntity> userEntityList = userRepository.findAll();
        int nowTime = LocalDateTime.now().getHour()*60+LocalDateTime.now().getMinute();
        if(nowTime>=1430) nowTime -= 1440;
        int finalNowTime = nowTime;
        userEntityList.forEach((user) -> {
            String alertTime = user.getAlertTime();
            if(Objects.equals(alertTime, "알림 안씀")){
                return;
            }
            String[] hour = alertTime.split(":");
            int setTime = Integer.parseInt(hour[0])*60+Integer.parseInt(hour[1]);
            log.info("{}",setTime);
            log.info("{}",finalNowTime);
            if(Math.abs(setTime - finalNowTime) > 10){
                return;
            }
            Hibernate.initialize(user.getWork());
            List<WorkEntity> workEntityList = user.getWork();
            boolean isAlert = workEntityList.stream().anyMatch((work)->{
                log.info("{}",streakBusiness.isValidExtend(work.getId()));
                return streakBusiness.isValidExtend(work.getId());
            });
            if(isAlert){
                List<FirebaseEntity> firebaseEntityList = user.getFirebase();
                firebaseEntityList.forEach((firebase) -> {
                    firebaseService.alert(firebase.getToken(), "오늘 스트릭이 만료됩니다!", "남은 시간이 얼마 없습니다. 스트릭을 유지하려면 지금 목표를 완료하세요!");
                });
            }
        });
    }

    public Api<String> setAlertTime(UserEntity user, String alertTime) {
        user.setAlertTime(alertTime);
        userRepository.save(user);
        return Api.OK("설정되었습니다");
    }
}

package com.example.streak.user.service;

import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.email.db.EmailAuthEntity;
import com.example.streak.email.db.EmailRepository;
import com.example.streak.email.model.EmailRequest;
import com.example.streak.email.service.EmailService;
import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.model.UserChangeRegister;
import com.example.streak.user.model.UserPasswordRequest;
import com.example.streak.user.model.UserRegisterRequest;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.db.enums.WorkState;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.service.WorkConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.streak.utils.Encrypt.encrypt;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final WorkConverter workConverter;
    private final EmailRepository emailRepository;
    private final EmailService emailService;

    public String register(UserRegisterRequest userRegisterRequest){
        Optional<EmailAuthEntity> _emailAuthEntity = emailRepository.findByEmail(userRegisterRequest.getName());
        if(_emailAuthEntity.isEmpty() || !_emailAuthEntity.get().getIsAuth()) {
            return "인증을 진행해 주세요";
        }

        String encryptPassword = encrypt(userRegisterRequest.getPassword());
        UserEntity user = UserEntity.builder()
                .createdAt(LocalDateTime.now())
                .name(userRegisterRequest.getName())
                .password(encryptPassword).build();
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
}

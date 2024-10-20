package com.example.streak.user.service;

import com.example.streak.user.db.UserEntity;
import com.example.streak.user.db.UserRepository;
import com.example.streak.user.model.UserRegisterRequest;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.model.WorkDTO;
import com.example.streak.work.service.WorkConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.streak.utils.Encrypt.encrypt;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WorkConverter workConverter;

    public String register(UserRegisterRequest userRegisterRequest){
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
        List<WorkDTO> workDTOs = works.stream().map(work -> workConverter.toDTO(work)).toList();
        return workDTOs;
    }
}

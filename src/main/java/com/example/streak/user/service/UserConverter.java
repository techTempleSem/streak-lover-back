package com.example.streak.user.service;

import com.example.streak.user.db.UserEntity;
import com.example.streak.user.model.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserConverter {
    public UserDTO toDTO(UserEntity userEntity){
        return UserDTO.builder()
                .name(userEntity.getName())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }
}

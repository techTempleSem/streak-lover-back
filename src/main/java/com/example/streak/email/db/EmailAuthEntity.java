package com.example.streak.email.db;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailAuthEntity {
    @Email
    private String email;

    private Boolean isAuth;
    private LocalDateTime authAt;
    private String code;
}

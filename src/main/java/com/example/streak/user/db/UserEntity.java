package com.example.streak.user.db;

import com.example.streak.firebase.db.FirebaseEntity;
import com.example.streak.user.db.enums.UserState;
import com.example.streak.work.db.WorkEntity;
import com.example.streak.work.db.enums.WorkState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity(name="user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String tempPassword;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "VARCHAR(45)")
    @Enumerated(EnumType.STRING)
    private UserState state;

    private Integer workCount;

    @OneToMany
    @JoinColumn(name="user_id")
    private List<WorkEntity> work;

    @OneToMany
    @JoinColumn(name="user_id")
    private List<FirebaseEntity> firebase;

    private String alertTime;
}

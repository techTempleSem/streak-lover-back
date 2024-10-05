package com.example.streak.work.db;

import com.example.streak.streak.db.StreakEntity;
import com.example.streak.user.db.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value= PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity(name="work")
public class WorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    private LocalDateTime createdAt;

    @OneToMany
    @JoinColumn(name="work_id")
    private List<StreakEntity> streak;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private UserEntity user;

    private Integer orderNum;
}

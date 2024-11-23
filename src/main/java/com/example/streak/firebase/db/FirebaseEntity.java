package com.example.streak.firebase.db;

import com.example.streak.user.db.UserEntity;
import com.example.streak.work.db.WorkEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity(name="firebase")
public class FirebaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private UserEntity user;
}

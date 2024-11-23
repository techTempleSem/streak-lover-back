package com.example.streak.work.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value= PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Integer orderNum;
    private LocalDateTime lastUpdatedAt;
    private Integer curStreak;
    private Integer dayWeek;
    private Integer money;
    private Integer repair;
}

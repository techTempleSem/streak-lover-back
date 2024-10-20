package com.example.streak.work.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WorkRegisterRequest {
    @NonNull
    private String title;
    private String description;

    @JsonProperty("selectedDays")
    private Map<String, Boolean> selectedDays;
}

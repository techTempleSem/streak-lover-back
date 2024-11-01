package com.example.streak.work.db.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum WorkState {
    NORMAL("일반"),
    DELETE("삭제");
    private String description;

}

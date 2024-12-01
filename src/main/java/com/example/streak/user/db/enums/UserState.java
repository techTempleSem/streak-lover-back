package com.example.streak.user.db.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserState {
    NORMAL("일반"),
    OWNER("관리자"),
    DELETE("삭제");
    private String description;
}

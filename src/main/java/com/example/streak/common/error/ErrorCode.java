package com.example.streak.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeIfs {
    OK(200, 200, "성공"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버 에러"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 1401, "로그인을 해 주세요"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 1400, "잘못된 요청");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}

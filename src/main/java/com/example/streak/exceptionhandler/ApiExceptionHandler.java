package com.example.streak.exceptionhandler;

import com.example.streak.common.api.Api;
import com.example.streak.common.error.ErrorCode;
import com.example.streak.common.exception.ApiException;
import com.example.streak.common.exception.ApiExceptionIfs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Api<Object>> exception(
            ApiException e
    ){
        log.error("", e);
        var errorCode = e.getErrorCodeIfs();
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(
                        Api.ERROR(errorCode, e.getErrorDescription())
                );
    }
}

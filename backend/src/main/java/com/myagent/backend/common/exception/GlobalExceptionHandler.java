package com.myagent.backend.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // "모든 컨트롤러에서 튀어오르는 예외를 내가 지켜봄"
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class) // "그중 BusinessException이 오면 이 메서드가 잡음"
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode));
    }
}

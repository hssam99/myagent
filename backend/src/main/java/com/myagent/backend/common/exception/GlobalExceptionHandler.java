package com.myagent.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()// TODO: 필드 여러 개면 에러 목록으로 응답
                .map(error -> error.getDefaultMessage())
                .orElse("잘못된 입력입니다");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)   // 400
                .body(new ErrorResponse("INVALID_INPUT", message));
    }
}

package com.myagent.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/*
HttpStatus.OK           = 200  (성공)
HttpStatus.CREATED      = 201  (생성됨)
HttpStatus.NOT_FOUND    = 404  (없음)
HttpStatus.FORBIDDEN    = 403  (권한 없음)
HttpStatus.UNAUTHORIZED = 401  (로그인 안 됨)
 */


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "캘린더를 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다.");

    private final HttpStatus status;
    private final String message;
}

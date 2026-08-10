package com.myagent.backend.calendar.controller;

import com.myagent.backend.calendar.dto.CalendarCreateRequest;
import com.myagent.backend.calendar.dto.CalendarResponse;
import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.calendar.service.CalendarService;
import com.myagent.backend.user.entity.User;
import com.myagent.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserService userService;

    // TODO: 없는 캘린더 조회 구현 시 전역 예외 처리(@RestControllerAdvice + ErrorCode) + @Valid 입구 검증 도입
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 이 메서드 성공 시엔 200 대신 201을 보내라"는 선언
    public CalendarResponse create(@Valid @RequestBody CalendarCreateRequest request,
                                   OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        Calendar calendar = calendarService.create(me, request.name());
        return CalendarResponse.from(calendar);
    }

    @GetMapping
    public List<CalendarResponse> myCalendars(OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        return calendarService.getMyCalendars(me).stream()
                .map(CalendarResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public CalendarResponse getCalendar(@PathVariable Long id, OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        Calendar calendar = calendarService.getCalendarById(id, me);
        return CalendarResponse.from(calendar);
    }
}

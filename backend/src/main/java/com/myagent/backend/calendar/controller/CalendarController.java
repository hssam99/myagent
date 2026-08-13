package com.myagent.backend.calendar.controller;

import com.myagent.backend.calendar.dto.CalendarCreateRequest;
import com.myagent.backend.calendar.dto.CalendarResponse;
import com.myagent.backend.calendar.dto.CalendarUpdateRequest;
import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.calendar.service.CalendarService;
import com.myagent.backend.topic.dto.TopicResponse;
import com.myagent.backend.user.entity.User;
import com.myagent.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
public class CalendarController {

    private final UserService userService;
    private final CalendarService calendarService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 이 메서드 성공 시엔 200 대신 201을 보내라"는 선언
    public CalendarResponse create(@Valid @RequestBody CalendarCreateRequest req,
                                   OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        Calendar calendar = calendarService.create(me, req.name(), req.topicIds());
        return CalendarResponse.from(calendar, me);
    }

    @GetMapping
    public List<CalendarResponse> myCalendars(OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        return calendarService.getMyCalendars(me).stream()
                .map(c -> CalendarResponse.from(c, me))
                .toList();
    }

    @GetMapping("/{id}")
    public CalendarResponse getCalendar(@PathVariable Long id, OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        Calendar calendar = calendarService.getCalendarById(id, me);
        return CalendarResponse.from(calendar, me);
    }

    @PatchMapping("/{id}")
    public CalendarResponse rename(@PathVariable Long id,
                                   @Valid @RequestBody CalendarUpdateRequest req,
                                   OAuth2AuthenticationToken authentication){
        User me = userService.getCurrentUser(authentication);
        Calendar calendar = calendarService.rename(id, req.name(), me);
        return CalendarResponse.from(calendar, me);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        calendarService.delete(id,me);
    }

    @GetMapping("/{calendarId}/topics")
    public List<TopicResponse> getCalendarTopics(@PathVariable Long calendarId, OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        return calendarService.getCalendarTopics(calendarId, me).stream()
                .map(TopicResponse::from)
                .toList();
    }

    @PutMapping("/{calendarId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)      // 204, 본문 없음
    public void followTopic (@PathVariable Long calendarId, @PathVariable Long topicId, OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        calendarService.followTopic(calendarId,topicId,me);
    }

    @DeleteMapping("/{calendarId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)      // 204, 본문 없음
    public void unfollowTopic (@PathVariable Long calendarId, @PathVariable Long topicId, OAuth2AuthenticationToken authentication) {
        User me = userService.getCurrentUser(authentication);
        calendarService.unfollowTopic(calendarId,topicId,me);
    }
}

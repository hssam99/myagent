package com.myagent.backend.event.controller;

import com.myagent.backend.event.dto.EventCreateRequest;
import com.myagent.backend.event.dto.EventResponse;
import com.myagent.backend.event.entity.Event;
import com.myagent.backend.event.service.EventService;
import com.myagent.backend.user.entity.User;
import com.myagent.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendars/{calendarId}/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse create(@PathVariable Long calendarId,
                                @Valid @RequestBody EventCreateRequest request,
                                OAuth2AuthenticationToken authentication){

        User me = userService.getCurrentUser(authentication);
        Event event = eventService.createEvent(calendarId, me, request);
        return EventResponse.from(event);
    }

    @GetMapping
    public List<EventResponse> calendarEvents(@PathVariable Long calendarId, OAuth2AuthenticationToken authentication){
        User me = userService.getCurrentUser(authentication);
        return eventService.getCalendarEvents(calendarId, me).stream()
                .map(EventResponse::from)
                .toList();
    }
}

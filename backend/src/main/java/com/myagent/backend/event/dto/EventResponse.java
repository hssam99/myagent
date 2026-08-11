package com.myagent.backend.event.dto;

import com.myagent.backend.event.entity.Event;

import java.time.Instant;

// 프론트에 보이는 event 데이터

public record EventResponse(
        Long id,
        String title,
        Instant startsAt,
        Instant endsAt,
        boolean allDay,
        String place,
        String memo
        ) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.isAllDay(),
                event.getPlace(),
                event.getMemo()
        );
    }
}
package com.myagent.backend.calendar.dto;

import com.myagent.backend.calendar.entity.Calendar;

public record CalendarResponse(Long id, String name, String inviteCode) {
    public static CalendarResponse from(Calendar calendar) {
        return new CalendarResponse(calendar.getId(), calendar.getName(), calendar.getInviteCode());
    }
}

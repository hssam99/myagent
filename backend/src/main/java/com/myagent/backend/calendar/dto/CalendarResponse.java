package com.myagent.backend.calendar.dto;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.user.entity.User;

public record CalendarResponse(Long id, String name, String inviteCode, boolean owner) {
    public static CalendarResponse from(Calendar calendar, User me) {
//        접근한 유저가 owner일 경우 true / 아니면 false
        return new CalendarResponse(calendar.getId(), calendar.getName(), calendar.getInviteCode(), calendar.getOwner().equals(me));
    }
}

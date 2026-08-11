package com.myagent.backend.event.service;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.calendar.service.CalendarService;
import com.myagent.backend.event.dto.EventCreateRequest;
import com.myagent.backend.event.entity.Event;
import com.myagent.backend.event.repository.EventRepository;
import com.myagent.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final CalendarService calendarService;

    // TODO: allday, endsAt에 대한 검증은 나중에 추가 (endsAt >= startsAt)
    @Transactional
    public Event createEvent(Long calendarId, User owner, EventCreateRequest request) {
        Calendar calendar = calendarService.getCalendarById(calendarId, owner);
        Event event = Event.builder()
                .calendar(calendar)
                .title(request.title())
                .place(request.place())
                .startsAt(request.startsAt())
                .endsAt(request.endsAt())
                .allDay(request.allDay())
                .memo(request.memo())
                .build();

        return eventRepository.save(event);
    }

    public List<Event> getCalendarEvents(Long calendarId, User me) {
        Calendar calendar = calendarService.getCalendarById(calendarId, me);
        return eventRepository.findByCalendar(calendar);
    }
}

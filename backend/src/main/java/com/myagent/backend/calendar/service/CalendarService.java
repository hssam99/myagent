package com.myagent.backend.calendar.service;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.calendar.repository.CalendarRepository;
import com.myagent.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Transactional
    public Calendar create(User owner, String name){
        String inviteCode = UUID.randomUUID().toString().replace("-", "");

        Calendar calendar = Calendar.builder()
                .owner(owner)
                .name(name)
                .inviteCode(inviteCode)
                .build();

        return calendarRepository.save(calendar);
    }

    public List<Calendar> getMyCalendars(User owner){
        return calendarRepository.findByOwner(owner);
    }
}

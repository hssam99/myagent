package com.myagent.backend.calendar.service;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.calendar.repository.CalendarRepository;
import com.myagent.backend.common.exception.BusinessException;
import com.myagent.backend.common.exception.ErrorCode;
import com.myagent.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    public Calendar getCalendarById(Long id, User owner){
        Calendar calendar = calendarRepository.findById(id).orElseThrow(()
                -> new BusinessException(ErrorCode.CALENDAR_NOT_FOUND));
        if(!calendar.getOwner().equals(owner)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        return calendar;
    }
}

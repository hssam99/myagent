package com.myagent.backend.calendar.service;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.calendar.entity.CalendarTopic;
import com.myagent.backend.calendar.repository.CalendarRepository;
import com.myagent.backend.calendar.repository.CalendarTopicRepository;
import com.myagent.backend.common.exception.BusinessException;
import com.myagent.backend.common.exception.ErrorCode;
import com.myagent.backend.event.repository.EventRepository;
import com.myagent.backend.topic.entity.Topic;
import com.myagent.backend.topic.service.TopicService;
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
    private final CalendarTopicRepository calendarTopicRepository;
    private final EventRepository eventRepository;
    private final TopicService topicService;

    @Transactional
    public Calendar create(User owner, String name, List<Long> topicIds) {
        String inviteCode = UUID.randomUUID().toString().replace("-", "");

        Calendar calendar = calendarRepository.save(
                Calendar.builder().owner(owner).name(name).inviteCode(inviteCode).build()
        );

        if (topicIds != null) { topicIds.forEach(topicId -> addTopic(calendar, topicId)); }
        return calendar;
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

    @Transactional
    public void delete(Long id, User me) {
        Calendar calendar = getCalendarById(id, me);
        // 상속된 것 먼저 제거
        eventRepository.deleteByCalendar(calendar);
        calendarTopicRepository.deleteByCalendar(calendar);
        // 캘린더 제거
        calendarRepository.delete(calendar);
    }

    public List<Topic> getCalendarTopics(Long id, User owner) {
        Calendar calendar = getCalendarById(id, owner);
        return calendarTopicRepository.findByCalendar(calendar).stream()
                .map(CalendarTopic::getTopic)
                .toList();
    }

    @Transactional
    public void followTopic(Long calendarId, Long topicId, User owner) {
        addTopic(getCalendarById(calendarId, owner), topicId);
    }

    @Transactional
    public void unfollowTopic(Long calendarId, Long topicId, User me) {
        Calendar calendar = getCalendarById(calendarId, me);
        Topic topic = topicService.getTopicById(topicId);

        calendarTopicRepository.findByCalendarAndTopic(calendar, topic)
                .ifPresent(calendarTopicRepository::delete);
    }

    private void addTopic(Calendar calendar, Long topicId) {
        Topic topic = topicService.getTopicById(topicId);
        if (!calendarTopicRepository.existsByCalendarAndTopic(calendar, topic)) {
            calendarTopicRepository.save(
                    CalendarTopic.builder().calendar(calendar).topic(topic).build()
            );
        }
    }

    @Transactional
    public Calendar rename(Long id, String name, User me) {
        Calendar calendar = getCalendarById(id, me);
        calendar.updateName(name);
        return calendar;
    }
}

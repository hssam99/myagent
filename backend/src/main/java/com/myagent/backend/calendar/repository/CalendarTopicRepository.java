package com.myagent.backend.calendar.repository;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.calendar.entity.CalendarTopic;
import com.myagent.backend.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarTopicRepository extends JpaRepository<CalendarTopic,Long> {
    List<CalendarTopic> findByCalendar(Calendar calendar);

    boolean existsByCalendarAndTopic(Calendar calendar, Topic topic);

    Optional<CalendarTopic> findByCalendarAndTopic(Calendar calendar, Topic topic);

    void deleteByCalendar(Calendar calendar);
}

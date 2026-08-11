package com.myagent.backend.event.repository;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByCalendar(Calendar calendar);
}

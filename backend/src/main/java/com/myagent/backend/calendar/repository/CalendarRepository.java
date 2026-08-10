package com.myagent.backend.calendar.repository;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    List<Calendar> findByOwner(User owner);
}

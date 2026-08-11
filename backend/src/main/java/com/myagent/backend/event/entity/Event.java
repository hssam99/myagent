package com.myagent.backend.event.entity;

import com.myagent.backend.calendar.entity.Calendar;
import com.myagent.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event  extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="calendar_id", nullable = false)
    private Calendar calendar;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private Instant startsAt;

    private Instant endsAt;

    @Column(nullable = false)
    private boolean allDay;

    @Column(length = 100)
    private String place;

    @Column(length = 200)
    private String memo;

    @Builder
    public Event(Calendar calendar, String title, Instant startsAt, Instant endsAt, boolean allDay, String place, String memo) {
        this.calendar = calendar;
        this.title = title;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.allDay = allDay;
        this.place = place;
        this.memo = memo;
    }

}

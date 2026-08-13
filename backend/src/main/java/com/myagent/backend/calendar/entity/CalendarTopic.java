package com.myagent.backend.calendar.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import com.myagent.backend.topic.entity.Topic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="calendar_topics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarTopic extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="calendar_id", nullable = false)
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="topic_id", nullable = false)
    private Topic topic;

    @Builder
    public CalendarTopic(Long id, Calendar calendar, Topic topic) {
        this.id = id;
        this.calendar = calendar;
        this.topic = topic;
    }
}

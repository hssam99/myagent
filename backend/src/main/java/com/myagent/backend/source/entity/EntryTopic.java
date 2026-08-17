package com.myagent.backend.source.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import com.myagent.backend.topic.entity.Topic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="entry_topics",
uniqueConstraints = {@UniqueConstraint(
        name="uk_entry_topics_entry_topic",
        columnNames = {"entry_id", "topic_id"}
        )})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryTopic extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="entry_id", nullable = false)
    private Entry entry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Builder
    public EntryTopic(Entry entry, Topic topic) {
        this.entry = entry;
        this.topic = topic;
    }

}

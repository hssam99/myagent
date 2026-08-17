package com.myagent.backend.source.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import com.myagent.backend.topic.entity.Topic;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="item_topics",
uniqueConstraints = {@UniqueConstraint(
        name="uk_item_topics_item_topic",
        columnNames = {"item_id", "topic_id"}
        )})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemTopic extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Builder
    public ItemTopic(Item item, Topic topic) {
        this.item = item;
        this.topic = topic;
    }
}

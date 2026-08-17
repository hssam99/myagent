package com.myagent.backend.topic.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "topic_aliases",
uniqueConstraints = {@UniqueConstraint(
        name="uk_aliases_topic_alias",
        columnNames = {"topic_id", "alias"}
)})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopicAlias extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="topic_id", nullable = false)
    private Topic topic;

    @Column(length = 100, nullable = false)
    private String alias;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MatchType matchType;

    @Builder
    public TopicAlias(Topic topic, String alias, MatchType matchType) {
        this.topic = topic;
        this.alias = alias;
        this.matchType = matchType;
    }
}

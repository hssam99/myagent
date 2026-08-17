package com.myagent.backend.source.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import com.myagent.backend.topic.entity.Topic;
import com.myagent.backend.topic.entity.TopicCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "sources")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Source extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    // 어떤 Collector를 사용할지 결정 (rss, api, ..)
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private SourceType type;

    // 특정 Topic(아이브, 토트넘, 페라리)과 관련
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="topic_id")
    private Topic topic;

//  특정 카테고리(kpop, epl)과 관련
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TopicCategory category;

    @Column(length = 500, nullable = false, unique = true)
    private String sourceUrl;

    @Column(length = 100)
    private String bodySelector;

    @Column(nullable = false)
    private boolean enabled = true;

    private Instant lastFetchedAt;

    @Column(nullable = false)
    private int failCount = 0;

    @Builder
    public Source(
            String name,
            SourceType type,
            Topic topic,
            TopicCategory category,
            String sourceUrl,
            String bodySelector
    ) {
        this.name = name;
        this.type = type;
        this.topic = topic;
        this.category = category;
        this.sourceUrl = sourceUrl;
        this.bodySelector = bodySelector;
    }
}

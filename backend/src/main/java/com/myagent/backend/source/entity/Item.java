package com.myagent.backend.source.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "items",
uniqueConstraints = @UniqueConstraint(
        name = "uk_items_source_external_id",
        columnNames = {"source_id", "external_id"}
))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @Column(length = 500, nullable = false)
    private String externalId;

    @Column(length = 500, nullable = false)
    private String url;

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private Instant publishedAt;

    @Builder
    public Item(Source source,
                String externalId,
                String url,
                String title,
                String body,
                Instant publishedAt) {
        this.source = source;
        this.externalId = externalId;
        this.url = url;
        this.title = title;
        this.body = body;
        this.publishedAt = publishedAt;
    }
}

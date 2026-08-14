package com.myagent.backend.source.entity;

import com.myagent.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "entries",
uniqueConstraints = @UniqueConstraint(
        name = "uk_entries_source_guid",
        columnNames = {"source_id", "guid"}
))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Entry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @Column(length = 500, nullable = false)
    private String guid;

    @Column(length = 500, nullable = false)
    private String url;

    @Column(length = 300)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private Instant publishedAt;

    @Builder
    public Entry(Source source,
                 String guid,
                 String url,
                 String title,
                 String body,
                 Instant publishedAt) {
        this.source = source;
        this.guid = guid;
        this.url = url;
        this.title = title;
        this.body = body;
        this.publishedAt = publishedAt;
    }
}

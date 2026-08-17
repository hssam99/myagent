package com.myagent.backend.source.dto;

import java.time.Instant;

public record ItemData(
        String externalId,
        String url,
        String title,
        Instant publishedAt
) {
}

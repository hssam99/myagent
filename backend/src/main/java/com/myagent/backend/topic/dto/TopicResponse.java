package com.myagent.backend.topic.dto;

import com.myagent.backend.topic.entity.Topic;
import com.myagent.backend.topic.entity.TopicCategory;

public record TopicResponse(Long id, String name, TopicCategory category) {
    public static TopicResponse from(Topic topic) {
        return new TopicResponse(topic.getId(), topic.getName(), topic.getCategory());
    }
}

package com.myagent.backend.topic.controller;

import com.myagent.backend.topic.dto.TopicResponse;
import com.myagent.backend.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public List<TopicResponse> getTopics() {
        return topicService.getAllTopic().stream()
                .map(TopicResponse::from)
                .toList();
    }
}

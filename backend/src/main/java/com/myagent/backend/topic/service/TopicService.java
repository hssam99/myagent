package com.myagent.backend.topic.service;

import com.myagent.backend.common.exception.BusinessException;
import com.myagent.backend.common.exception.ErrorCode;
import com.myagent.backend.topic.entity.Topic;
import com.myagent.backend.topic.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public List<Topic> getAllTopic(){
        return topicRepository.findAll();
    };

    public Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(()-> new BusinessException(ErrorCode.TOPIC_NOT_FOUND));
    }
}

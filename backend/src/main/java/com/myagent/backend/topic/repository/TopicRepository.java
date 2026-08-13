package com.myagent.backend.topic.repository;

import com.myagent.backend.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}

package com.myagent.backend.topic.repository;

import com.myagent.backend.topic.entity.TopicAlias;
import com.myagent.backend.topic.entity.TopicCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicAliasRepository extends JpaRepository<TopicAlias,Long> {
    List<TopicAlias> findByTopicCategory(TopicCategory category);

    List<TopicAlias> findByTopicId(Long id);
}

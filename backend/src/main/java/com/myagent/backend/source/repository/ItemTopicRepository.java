package com.myagent.backend.source.repository;

import com.myagent.backend.source.entity.ItemTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTopicRepository extends JpaRepository<ItemTopic,Long> {
}

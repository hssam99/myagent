package com.myagent.backend.source.repository;

import com.myagent.backend.source.entity.Item;
import com.myagent.backend.source.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
    boolean existsBySourceAndGuid(Source source, String guid);
}

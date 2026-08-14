package com.myagent.backend.source.repository;

import com.myagent.backend.source.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceRepository extends JpaRepository<Source,Long> {
    List<Source> findByEnabledTrue();
}

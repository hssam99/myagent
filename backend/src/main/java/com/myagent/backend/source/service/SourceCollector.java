package com.myagent.backend.source.service;

import com.myagent.backend.source.dto.ItemData;
import com.myagent.backend.source.entity.Source;

import java.util.List;

// 데이터 변환기 인터페이스
public interface SourceCollector {
    List<ItemData> collect(Source source);
}

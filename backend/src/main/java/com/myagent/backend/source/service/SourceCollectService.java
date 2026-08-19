package com.myagent.backend.source.service;

import com.myagent.backend.source.dto.ItemData;
import com.myagent.backend.source.entity.Item;
import com.myagent.backend.source.entity.Source;
import com.myagent.backend.source.repository.SourceRepository;
import com.myagent.backend.topic.service.TopicMatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SourceCollectService {

    private final SourceRepository sourceRepository;
    private final RssSourceCollector rssSourceCollector;
    private final TopicMatchingService topicMatchingService;
    private final ItemService itemService;

    // db(source)에 등록된 매체, 소스(ex. 스타뉴스) 모두 가져오기
    public void collectAll() {
        List<Source> sourceList = sourceRepository.findByEnabledTrue();
        for (Source source : sourceList) {
            try {
                collect(source);
                // TODO: source.markFetchSuccess();
            } catch (Exception e) {
                log.error("수집 실패: {}", source.getName(), e);
                // TODO: source.markFetchFailure();
            }
        }
    }

    // 내용(item) 가져오기 (rss, api 등) -> item db에 저장
    private void collect(Source source) {
        //TODO: 스위치문 길어지면 Map<SourceType, SourceCollector> 리팩토링
        // 1. 소스 타입에 맞는 방식으로 데이터 수집
        SourceCollector sourceCollector = switch (source.getType()) {
            case RSS -> rssSourceCollector;
            case KOPIS_API -> throw new IllegalStateException("아직 지원하지 않는 소스 타입: " + source.getType()); // TODO
        };
        List<ItemData> items = sourceCollector.collect(source);

        // 2. Item 저장 + 중복 제거
        List<Item> newItems = itemService.saveAll(source, items);
        log.info("수집 완료: {}, 신규 {}건 / 전체 {}건", source.getName(), newItems.size(), items.size());

        // 3. 새로들어온 기사 제목/요약 기반 토픽 1차 매칭
        topicMatchingService.matchByTitle(newItems, source);
    }
}

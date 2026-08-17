package com.myagent.backend.source.service;

import com.myagent.backend.source.dto.ItemData;
import com.myagent.backend.source.entity.Item;
import com.myagent.backend.source.entity.Source;
import com.myagent.backend.source.repository.ItemRepository;
import com.myagent.backend.source.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SourceCollectService {

    private final SourceRepository sourceRepository;
    private final ItemRepository itemRepository;
    private final RssSourceCollector rssSourceCollector;

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

    // 내용(entry) 가져오기 (rss, api 등) -> entry db에 저장
    //TODO: 스위치문 길어지면 Map<SourceType, SourceCollector> 리팩토링
    private void collect(Source source) {
        SourceCollector collector = switch (source.getType()) {
            case RSS -> rssSourceCollector;
            case KOPIS_API -> throw new IllegalStateException("아직 지원하지 않는 소스 타입: " + source.getType()); // TODO
        };
        List<ItemData> items = collector.collect(source);
        int saveCount = 0;

        // 데이터 확인 > items 저장
        for (ItemData item : items) {
            if(itemRepository.existsBySourceAndExternalId(source, item.externalId())){continue;}
            itemRepository.save(
                    Item.builder()
                            .source(source)
                            .externalId(item.externalId())
                            .url(item.url())
                            .title(item.title())
                            .publishedAt(item.publishedAt())
                            .build()
            );
            saveCount++;
        }
        log.info("수집 완료: {}, 신규 {}건 / 전체 {}건",  source.getName(), saveCount,items.size());
    }

}

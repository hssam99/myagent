package com.myagent.backend.source.service;

import com.myagent.backend.source.entity.Item;
import com.myagent.backend.source.entity.Source;
import com.myagent.backend.source.repository.ItemRepository;
import com.myagent.backend.source.repository.SourceRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SourceCollectService {

    private final SourceRepository sourceRepository;
    private final ItemRepository itemRepository;

    // db(source)에 등록된 매체, 소스(ex. 스타뉴스) 모두 가져오기
    public void collectAll() {
        List<Source> sourceList = sourceRepository.findByEnabledTrue();
        for (Source source : sourceList) {collect(source);}

    }

    // 내용(entry) 가져오기 (rss, api 등) -> entry db에 저장
    private void collect(Source source) {
        try {
            // TODO: source.type에 따라 분기 (현재 RSS만)
            SyndFeedInput input = new SyndFeedInput(); // ROME
            SyndFeed feed = input.build(new XmlReader(new URL(source.getSourceUrl()))); // 소스 url xml 읽어오기
            List<SyndEntry> entries = feed.getEntries(); // 기사 목록 불러오기
            log.info("수집 시작: {}, 기사 {}건", source.getName(), entries.size());

            int saveCount = 0;

            // 기사 확인
            for (SyndEntry entry : entries) {
                String url = entry.getLink();
                if (url == null || url.isBlank()) {continue;}

                String guid = entry.getUri();
                if (guid == null || guid.isBlank()) { guid = url;}

                if(itemRepository.existsBySourceAndGuid(source,guid)){continue;}

                Instant publishedAt = null;
                if (entry.getPublishedDate() != null) {
                    publishedAt = entry.getPublishedDate().toInstant();
                }
               itemRepository.save(
                       Item.builder()
                               .source(source)
                               .guid(guid)
                               .url(url)
                               .title(entry.getTitle())
                               .publishedAt(publishedAt)
                               .build()
               );
                log.info("제목: {} / link: {} / guid: {}", entry.getTitle(), url, guid);
                saveCount++;
            }
            log.info("수집 완료: {}, 신규 {}건 / 전체 {}건",  source.getName(), saveCount,entries.size());


        } catch (Exception e) {
            // 도중에 뭐가 터지면 (주소 죽음, 네트워크 등) 기록하고 넘어가기
            log.error("수집 실패: {}", source.getName(), e);
        }
    }

}

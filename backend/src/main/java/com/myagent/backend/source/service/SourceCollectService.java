package com.myagent.backend.source.service;

import com.myagent.backend.source.entity.Source;
import com.myagent.backend.source.repository.SourceRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SourceCollectService {

    private final SourceRepository sourceRepository;

    // db에 등록된 소스 모두 가져오기
    public void collectAll() {
        List<Source> sourceList = sourceRepository.findByEnabledTrue();
        for (Source source : sourceList) {collect(source);}

    }

    private void collect(Source source) {
        try {
            SyndFeedInput input = new SyndFeedInput(); // ROME
            SyndFeed feed = input.build(new XmlReader(new URL(source.getSourceUrl()))); // 소스 url xml 읽어오기
            List<SyndEntry> entries = feed.getEntries(); // 기사 목록 불러오기
            log.info("수집 시작: {}, 기사 {}건", source.getName(), entries.size());

            // 기사 확인
            for (SyndEntry entry : entries) {
                log.info("제목: {} / link: {} / guid: {}",
                        entry.getTitle(), entry.getLink(), entry.getUri());
            }
        } catch (Exception e) {
            // 도중에 뭐가 터지면 (주소 죽음, 네트워크 등) 기록하고 넘어가기
            log.error("수집 실패: {}", source.getName(), e);
        }
    }

}

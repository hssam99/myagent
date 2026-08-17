package com.myagent.backend.source.service;

import com.myagent.backend.source.dto.ItemData;
import com.myagent.backend.source.entity.Source;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// RSS 소스 수집 → ItemData 변환
@Component
public class RssSourceCollector implements SourceCollector {
    @Override
    public List<ItemData> collect(Source source) {
        try{
            SyndFeedInput input = new SyndFeedInput(); // ROME 번역기 생성
            SyndFeed feed = input.build(new XmlReader(new URL(source.getSourceUrl()))); // 소스 url xml 읽어오기
            List<SyndEntry> articles = feed.getEntries(); // 기사 목록 불러오기
            List<ItemData> result = new ArrayList<>();

            for (SyndEntry article: articles){
                String url = article.getLink();
                if (url == null || url.isBlank()) {continue;}

                String externalId = article.getUri();
                if (externalId == null || externalId.isBlank()) { externalId = url;}

                Instant publishedAt = null;
                if (article.getPublishedDate() != null) {
                    publishedAt = article.getPublishedDate().toInstant();
                }
                result.add(new ItemData(externalId, url, article.getTitle(),  publishedAt));
            }
            return result;
        }catch (Exception e){
            //TODO: SourceCollectException 생성
            throw new RuntimeException("RSS 수집 실패: " + source.getName(), e);
        }
    }
}

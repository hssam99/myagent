package com.myagent.backend.topic.service;

import com.myagent.backend.source.entity.Item;
import com.myagent.backend.source.entity.ItemTopic;
import com.myagent.backend.source.entity.Source;
import com.myagent.backend.source.repository.ItemTopicRepository;
import com.myagent.backend.topic.entity.Topic;
import com.myagent.backend.topic.entity.TopicAlias;
import com.myagent.backend.topic.entity.TopicCategory;
import com.myagent.backend.topic.matcher.TopicMatcher;
import com.myagent.backend.topic.repository.TopicAliasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TopicMatchingService {
    private final TopicAliasRepository topicAliasRepository;
    private final ItemTopicRepository itemTopicRepository;
    private final TopicMatcher topicMatcher;

    public void matchByTitle(List<Item> newItems, Source source) {
        if (newItems == null || newItems.isEmpty()) { return; } // 새로 들어온 기사 없을경우
        List<TopicAlias> aliases = getCandidateAliases(source); // 관련된 별명 가져오기
        for (Item item : newItems) {
            Set<Topic> matchedTopics = topicMatcher.match(item.getTitle(), aliases);
            for (Topic topic : matchedTopics) {
                itemTopicRepository.save(ItemTopic.builder().item(item).topic(topic).build());
            }
        }
    }

    private List<TopicAlias> getCandidateAliases(Source source) {
        // 특정 토픽에 귀결되는 소스 (ex. 스타뉴스 연예)
        if (source.getTopic()!=null) { return topicAliasRepository.findByTopicId(source.getTopic().getId());}
        // 특정 카테고리에 귀결되는 소스 (ex. 아이브 공식 팬카페)
        if (source.getCategory()!=null) { return topicAliasRepository.findByTopicCategory(source.getCategory());}
        // 범용 소스
        return topicAliasRepository.findAll();
    }
}

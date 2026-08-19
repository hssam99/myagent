package com.myagent.backend.source.service;

import com.myagent.backend.source.dto.ItemData;
import com.myagent.backend.source.entity.Item;
import com.myagent.backend.source.entity.Source;
import com.myagent.backend.source.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> saveAll(Source source, List<ItemData> items) {
        List<Item> newItems = new ArrayList<>();
        // 데이터 확인 > items 저장
        for (ItemData item : items) {
            // db 중복 검사
            if(itemRepository.existsBySourceAndExternalId(source, item.externalId())){continue;}
            Item entity =  itemRepository.save(
                    Item.builder()
                            .source(source)
                            .externalId(item.externalId())
                            .url(item.url())
                            .title(item.title())
                            .publishedAt(item.publishedAt())
                            .build()
            );
            newItems.add(entity);
        }
        return newItems;
    }
}

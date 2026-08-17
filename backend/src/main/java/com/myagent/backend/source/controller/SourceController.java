package com.myagent.backend.source.controller;

import com.myagent.backend.source.service.SourceCollectService;
import lombok.RequiredArgsConstructor;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceCollectService sourceCollectService;

    @PostMapping("/collect")
    public ResponseEntity<Void> collect() {
        sourceCollectService.collectAll();
        return ResponseEntity.ok().build();
    }


    @PostMapping("/extract-test")
    public ResponseEntity<String> extractTest(@RequestParam String url) {
        try {
            String html = Jsoup.connect(url).userAgent("Mozilla/5.0").get().html();
            Article article = new Readability4J(url, html).parse();
            return ResponseEntity.ok(article.getTextContent());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("추출 실패: " + e.getMessage());
        }
    }


}

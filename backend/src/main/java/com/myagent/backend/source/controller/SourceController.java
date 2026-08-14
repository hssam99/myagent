package com.myagent.backend.source.controller;

import com.myagent.backend.source.service.SourceCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}

package com.springboot.api.controller;

import com.springboot.api.dto.NaverNewsResponse;
import com.springboot.api.service.NewsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsPageController {

    private final NewsPageService newsPageService;

    @GetMapping("/news")
    public Mono<List<NaverNewsResponse.Item>> getDefaultNews() {
        return newsPageService.getDefaultNews();
    }
}

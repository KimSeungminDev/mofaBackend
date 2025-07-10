package com.springboot.api.controller;

import com.springboot.api.dto.YouTubeVideoResponse;
import com.springboot.api.service.YouTubeVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class YouTubeVideoController {

    private final YouTubeVideoService youTubeVideoService;

    @GetMapping("/safetyvideo")
    public Mono<List<YouTubeVideoResponse>> getSafetyVideos() {
        return youTubeVideoService.getLatestVideos(20); // 원하는 개수로 고정
    }
}

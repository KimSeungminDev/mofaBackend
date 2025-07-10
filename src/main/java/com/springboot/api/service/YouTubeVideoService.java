package com.springboot.api.service;

import com.springboot.api.client.YouTubeVideoClient;
import com.springboot.api.dto.YouTubeVideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeVideoService {

    private final YouTubeVideoClient youTubeVideoClient;

    public Mono<List<YouTubeVideoResponse>> getLatestVideos(int maxResults) {
        return youTubeVideoClient.fetchRecentVideos(maxResults); // 그대로 전달
    }
}

package com.springboot.api.service;

import com.springboot.api.client.TravelVideoClient;
import com.springboot.api.dto.TravelVideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SafetyVideoService {

    private final TravelVideoClient travelVideoClient;

    public Mono<TravelVideoResponse> getSafetyVideos() {
        int page = 1;
        int perPage = 20; // 기본 영상 개수
        return travelVideoClient.fetchTravelVideo(page, perPage);
    }
}

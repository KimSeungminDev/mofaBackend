package com.springboot.api.controller;

import com.springboot.api.dto.TravelVideoResponse;
import com.springboot.api.service.SafetyVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SafetyVideoController {

    private final SafetyVideoService safetyVideoService;

    @GetMapping("/safetyvideo")
    public Mono<TravelVideoResponse> getSafetyVideos() {
        return safetyVideoService.getSafetyVideos();
    }
}

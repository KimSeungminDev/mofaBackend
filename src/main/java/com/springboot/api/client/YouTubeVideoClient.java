package com.springboot.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.api.dto.YouTubeVideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeVideoClient {

    private final WebClient webClient = WebClient.create("https://www.googleapis.com");

    @Value("${external.youtube.api.key}")
    private String apiKey;

    @Value("${external.youtube.channel-id}")
    private String channelId;

    public Mono<List<YouTubeVideoResponse>> fetchRecentVideos(int maxResults) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/search")
                        .queryParam("part", "snippet")
                        .queryParam("channelId", channelId)
                        .queryParam("maxResults", maxResults)
                        .queryParam("order", "date")
                        .queryParam("type", "video")
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class) // 1. JSON 문자열로 받아서
                .map(this::parseToYouTubeVideoList); // 2. 직접 파싱
    }

    private List<YouTubeVideoResponse> parseToYouTubeVideoList(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            List<YouTubeVideoResponse> videos = new ArrayList<>();

            for (JsonNode item : root.path("items")) {
                String videoId = item.path("id").path("videoId").asText();
                JsonNode snippet = item.path("snippet");

                videos.add(new YouTubeVideoResponse(
                        snippet.path("title").asText(),
                        snippet.path("publishedAt").asText(),
                        snippet.path("description").asText(),
                        "https://youtu.be/" + videoId,
                        "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg"
                ));
            }

            return videos;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

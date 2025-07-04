// TravelVideoClient.java - 외교부_해외안전여행홈페이지 영상 링크 목록 API 클라이언트

package com.springboot.api.client;

import com.springboot.api.dto.TravelVideoResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TravelVideoClient {

    private final WebClient odcloudWebClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public TravelVideoClient(@Qualifier("odcloudWebClient") WebClient odcloudWebClient) {
        this.odcloudWebClient = odcloudWebClient;
    }

    public Mono<TravelVideoResponse> fetchTravelVideo(int page, int perPage) {
        return odcloudWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/15121204/v1/uddi:003a5622-bb22-4046-a91b-d12bb606d8b6")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("page", page)
                        .queryParam("perPage", perPage)
                        .build())
                .retrieve()
                .bodyToMono(TravelVideoResponse.class);
    }
}

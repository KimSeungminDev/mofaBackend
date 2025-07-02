// ConsularFaqClient.java - 공공데이터 API: 해외안전여행 FAQ 조회

package com.springboot.api.client;

import com.springboot.api.dto.ConsularFaqResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConsularFaqClient {

    @Qualifier("odcloudWebClient")
    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<ConsularFaqResponse> fetchFaqs(int page, int perPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/15121/63/v1/uddi:c9a3185d-7fb6-45e4-aac1-86ba2ccc236b")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("page", page)
                        .queryParam("perPage", perPage)
                        .queryParam("returnType", "JSON")
                        .build())
                .retrieve()
                .bodyToMono(ConsularFaqResponse.class);
    }
}

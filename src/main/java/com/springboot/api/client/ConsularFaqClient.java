// ConsularFaqClient.java - 공공데이터 API: 해외안전여행 FAQ 조회

package com.springboot.api.client;

import com.springboot.api.dto.ConsularFaqResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ConsularFaqClient {

    private final WebClient odcloudWebClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    // @Qualifier는 생성자 주입에 직접 붙여야 확정됨
    public ConsularFaqClient(@Qualifier("odcloudWebClient") WebClient odcloudWebClient) {
        this.odcloudWebClient = odcloudWebClient;
    }

    public Mono<ConsularFaqResponse> fetchFaqs(int page, int perPage) {
        return odcloudWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/15121453/v1/uddi:c9a3185d-77b6-45e4-aac1-86ba2ccc236b")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("page", page)
                        .queryParam("perPage", perPage)
                        .queryParam("returnType", "JSON")
                        .build())
                .retrieve()
                .bodyToMono(ConsularFaqResponse.class);
    }
}

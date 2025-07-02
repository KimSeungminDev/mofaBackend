// GlobalEconomicTrendsClient.java - 공공데이터 API: 외교부_국제경제동향

package com.springboot.api.client;

import com.springboot.api.dto.GlobalEconomicTrendsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GlobalEconomicTrendsClient {

    private final WebClient webClient; // 기본 WebClient 사용

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<GlobalEconomicTrendsResponse> fetchEconomicTrends(int page, int perPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/globalEconomicTrendsService")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", perPage)
                        .build())
                .retrieve()
                .bodyToMono(GlobalEconomicTrendsResponse.class);
    }
}

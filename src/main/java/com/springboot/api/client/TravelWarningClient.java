// TravelWarningClient.java - 공공데이터 API: 외교부_여행경보제도 조회

package com.springboot.api.client;

import com.springboot.api.dto.TravelWarningResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TravelWarningClient {

    private final WebClient webClient;

    // 외교부_여행경보제도 API 키 (디코딩된 키 사용)
    @Value("${external.api.travel-warning.service-key}")
    private String serviceKey;

    public Mono<TravelWarningResponse> fetchTravelWarnings(String countryName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/TravelWarningServiceV3")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 10)
                        .queryParam("cond[country_name::EQ]", countryName)
                        .build())
                .retrieve()
                .bodyToMono(TravelWarningResponse.class);
    }
}

// TravelSpecialWarningClient.java - 공공데이터 API: 외교부_특별여행 정보제도

package com.springboot.api.client;

import com.springboot.api.dto.TravelSpecialWarningResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TravelSpecialWarningClient {

    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<TravelSpecialWarningResponse> fetchSpecialWarnings(String countryName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/TravelSpecialWarningServiceV3")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 10)
                        .queryParam("cond[country_name::EQ]", countryName)
                        .build())
                .retrieve()
                .bodyToMono(TravelSpecialWarningResponse.class);
    }
}

// CountryPoliticsClient.java - 외교부_국가·지역별 정치정보 API 클라이언트

package com.springboot.api.client;

import com.springboot.api.dto.CountryPoliticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountryPoliticsClient {

    @Qualifier("odcloudWebClient")
    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<CountryPoliticsResponse> fetchCountryPolitics(int page, int perPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/15076558/v1/uddi:866785f9-1f3f-413e-8588-defc889c7474")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("page", page)
                        .queryParam("perPage", perPage)
                        .build())
                .retrieve()
                .bodyToMono(CountryPoliticsResponse.class);
    }
}

// EmbassyOfficeClient.java - 공공데이터 API: 외교부_국가·지역별 재외공관 정보

package com.springboot.api.client;

import com.springboot.api.dto.EmbassyOfficeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmbassyOfficeClient {

    @Qualifier("odcloudWebClient")
    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<EmbassyOfficeResponse> fetchEmbassyOffices(int page, int perPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/15076569/v1/uddi:7692653c-21f9-4396-b6b3-f3f0cdbe9370")
                        .queryParam("page", page)
                        .queryParam("perPage", perPage)
                        .queryParam("returnType", "json")
                        .queryParam("serviceKey", serviceKey)
                        .build())
                .retrieve()
                .bodyToMono(EmbassyOfficeResponse.class);
    }
}

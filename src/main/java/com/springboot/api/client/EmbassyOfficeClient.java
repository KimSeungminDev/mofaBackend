package com.springboot.api.client;// EmbassyOfficeClient.java - 공공데이터 API: 외교부_국가·지역별 재외공관 정보


import com.springboot.api.dto.EmbassyOfficeResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service

public class EmbassyOfficeClient {

    private final WebClient odcloudWebClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public EmbassyOfficeClient(@Qualifier("odcloudWebClient") WebClient odcloudWebClient) {
        this.odcloudWebClient = odcloudWebClient;
    }

    public Mono<EmbassyOfficeResponse> fetchEmbassyOffices(int page, int perPage) {
        return odcloudWebClient.get()
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

    // 국가명 기준 필터링 메서드
    public Mono<EmbassyOfficeResponse> fetchEmbassyOffices(String countryName) {
        return fetchEmbassyOffices(1, 500)
                .map(fullResponse -> {
                    List<EmbassyOfficeResponse.EmbassyOfficeData> filtered = fullResponse.getData().stream()
                            .filter(item -> countryName.equalsIgnoreCase(item.getCountryName()))
                            .toList();

                    fullResponse.setData(filtered);
                    fullResponse.setCurrentCount(filtered.size());

                    return fullResponse;
                });
    }
}

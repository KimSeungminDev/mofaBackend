// CountrySituationClient.java - 외교부_국가·지역별 주요정세 정보 API 클라이언트

package com.springboot.api.client;

import com.springboot.api.dto.CountrySituationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountrySituationClient {

    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<CountrySituationResponse> fetchCountrySituation(String countryName, String isoCode, int startYear, int endYear, int page, int numOfRows) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/OverviewSituationService")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("cond[country_nm::EQ]", countryName)
                        .queryParam("cond[country_iso_alp2::EQ]", isoCode)
                        .queryParam("cond[year::GT]", startYear)
                        .queryParam("cond[year::LT]", endYear)
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", numOfRows)
                        .build())
                .retrieve()
                .bodyToMono(CountrySituationResponse.class);
    }
}

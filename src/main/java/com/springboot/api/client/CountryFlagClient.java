// CountryFlagClient.java - 외교부_국가∙지역별 국기 이미지 API 클라이언트

package com.springboot.api.client;

import com.springboot.api.dto.CountryFlagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountryFlagClient {

    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<CountryFlagResponse> fetchCountryFlag(String countryName, String isoCode, int page, int numOfRows) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/CountryFlagService2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("cond[country_nm::EQ]", countryName)
                        .queryParam("cond[country_iso_alp2::EQ]", isoCode)
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", numOfRows)
                        .build())
                .retrieve()
                .bodyToMono(CountryFlagResponse.class);
    }
    public Mono<CountryFlagResponse> fetchCountryFlag(String countryName) {
        return fetchCountryFlag(countryName, "", 1, 10); // ISO코드는 생략 가능
    }
}

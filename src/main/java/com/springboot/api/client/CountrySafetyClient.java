// CountrySafetyClient.java - 외교부_국가∙지역별 안전공지 API 클라이언트

package com.springboot.api.client;

import com.springboot.api.dto.CountrySafetyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountrySafetyClient {

    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<CountrySafetyResponse> fetchCountrySafety(String countryName, String countryIsoCode, int page, int numOfRows) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/CountrySafetyService6")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("cond[country_nm::EQ]", countryName)
                        .queryParam("cond[country_iso_alp2::EQ]", countryIsoCode)
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", numOfRows)
                        .queryParam("returnType", "JSON")
                        .build())
                .retrieve()
                .bodyToMono(CountrySafetyResponse.class);
    }
    // 간단 호출용 Wrapper 메서드
    public Mono<CountrySafetyResponse> fetchCountrySafety(String countryName) {
        return fetchCountrySafety(countryName, "", 1, 10); // ISO 코드: 빈값, 기본 페이지: 1, row: 10
    }
}

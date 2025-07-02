// TravelAlarmServiceClient.java - 공공데이터 API: 국가·지역별 여행경보정보 조회

package com.springboot.api.client;

import com.springboot.api.dto.TravelAlarmServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TravelAlarmServiceClient {

    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<TravelAlarmServiceResponse> fetchAlarmInfo(String countryName, String isoCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/TravelAlarmService2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("returnType", "JSON")
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 10)
                        .queryParam("cond[country_nm::EQ]", countryName)
                        .queryParam("cond[country_iso_alp2::EQ]", isoCode)
                        .build())
                .retrieve()
                .bodyToMono(TravelAlarmServiceResponse.class);
    }
}

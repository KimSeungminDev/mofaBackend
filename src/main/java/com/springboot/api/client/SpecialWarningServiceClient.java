// SpecialWarningServiceClient.java - 공공데이터 API: 외교부_국가·지역별 특별여행주의보

package com.springboot.api.client;

import com.springboot.api.dto.SpecialWarningServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SpecialWarningServiceClient {

    @Qualifier("webClient")  // baseUrl: https://apis.data.go.kr
    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    /**
     * 외교부_국가·지역별 특별여행주의보 조회
     *
     * @param page        페이지 번호
     * @param perPage     페이지당 항목 수
     * @param country     한글 국가명 (예: 가나)
     * @param countryCode ISO 2자리 국가코드 (예: GH)
     * @return SpecialWarningServiceResponse (Mono 비동기 응답)
     */
    public Mono<SpecialWarningServiceResponse> fetchSpecialWarnings(int page, int perPage, String country, String countryCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/1262000/SptravelWarningServiceV2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", perPage)
                        .queryParam("returnType", "JSON")
                        .queryParam("cond[country_nm::EQ]", country)
                        .queryParam("cond[country_iso_alp2::EQ]", countryCode)
                        .build())
                .retrieve()
                .bodyToMono(SpecialWarningServiceResponse.class);
    }
}

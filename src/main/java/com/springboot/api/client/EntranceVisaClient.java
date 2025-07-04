// EntranceVisaClient.java - 외교부_국가·지역별 입국허가요건 API 클라이언트

package com.springboot.api.client;

import com.springboot.api.dto.EntranceVisaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EntranceVisaClient {

    private final WebClient webClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public Mono<EntranceVisaResponse> fetchEntranceVisa(String countryName, String isoCode, int page, int numOfRows) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/1262000/EntranceVisaService2/getEntranceVisaList2")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("returnType", "JSON")
                            .queryParam("cond[country_nm::EQ]", countryName)
                            .queryParam("pageNo", page)
                            .queryParam("numOfRows", numOfRows);

                    if (isoCode != null && !isoCode.isBlank()) {
                        builder.queryParam("cond[country_iso_alp2::EQ]", isoCode);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(EntranceVisaResponse.class);
    }

    // 국가명만 받는 wrapper (나머지는 default 값으로)
    public Mono<EntranceVisaResponse> fetchEntranceVisa(String countryName) {
        return fetchEntranceVisa(countryName, null, 1, 10);
    }
}

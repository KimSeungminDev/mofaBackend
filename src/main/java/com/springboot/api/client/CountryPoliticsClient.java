// CountryPoliticsClient.java - 외교부_국가·지역별 정치정보 API 클라이언트

package com.springboot.api.client;

import com.springboot.api.dto.CountryPoliticsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CountryPoliticsClient {

    private final WebClient odcloudWebClient;

    @Value("${external.api.service-key.default}")
    private String serviceKey;

    public CountryPoliticsClient(@Qualifier("odcloudWebClient") WebClient odcloudWebClient) {
        this.odcloudWebClient = odcloudWebClient;
    }

    public Mono<CountryPoliticsResponse> fetchCountryPolitics(int page, int perPage) {
        return odcloudWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/15076558/v1/uddi:866785f9-1f3f-413e-8588-defc889c7474")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("page", page)
                        .queryParam("perPage", perPage)
                        .build())
                .retrieve()
                .bodyToMono(CountryPoliticsResponse.class);
    }

    public Mono<CountryPoliticsResponse> fetchCountryPolitics(String countryName) {
        return fetchCountryPolitics(1, 500) // 최대 500건 받아오기
                .map(fullResponse -> {
                    var body = fullResponse.getData();
                    if (body == null) return fullResponse;

                    var filteredItems = body.stream()
                            .filter(item -> countryName.equalsIgnoreCase(item.getCountryNameKo()))
                            .toList();

                    CountryPoliticsResponse filteredResponse = new CountryPoliticsResponse();
                    filteredResponse.setCurrentCount(filteredItems.size());
                    filteredResponse.setMatchCount(filteredItems.size());
                    filteredResponse.setPage(1);
                    filteredResponse.setPerPage(filteredItems.size());
                    filteredResponse.setTotalCount(filteredItems.size());
                    filteredResponse.setData(filteredItems);

                    return filteredResponse;
                });
    }
}


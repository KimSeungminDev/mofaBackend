package com.springboot.api.client;

import com.springboot.api.dto.NaverNewsResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NaverNewsClient {

    private final WebClient naverWebClient;

    @Value("${external.naver.client-id}")
    private String clientId;

    @Value("${external.naver.client-secret}")
    private String clientSecret;

    public NaverNewsClient(@Qualifier("naverWebClient") WebClient naverWebClient) {
        this.naverWebClient = naverWebClient;
    }

    /**
     * 기본값: display=10, start=1, sort=sim(정확도순)
     */
    public Mono<NaverNewsResponse> searchNews(String query) {
        return searchNews(query, 10, 1, "sim");
    }

    /**
     * 네이버 뉴스 검색 호출
     * @param query   검색어 (한글 그대로 전달; WebClient가 인코딩함)
     * @param display 1~100
     * @param start   1~1000
     * @param sort    sim | date
     */
    public Mono<NaverNewsResponse> searchNews(String query, int display, int start, String sort) {
        return naverWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/news.json")
                        .queryParam("query", query)
                        .queryParam("display", display)
                        .queryParam("start", start)
                        .queryParam("sort", sort)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(NaverNewsResponse.class);
    }
}

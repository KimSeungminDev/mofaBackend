package com.springboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /* ===== 1) 공통 빌더 ===== */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /* ===== 2) 공공데이터 기본 WebClient ===== */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .clone()                              // baseUrl 겹치지 않게 복제
                .baseUrl("https://apis.data.go.kr")
                .build();
    }


    /* ===== 3) ODcloud 전용 WebClient ===== */
    @Bean
    @Qualifier("odcloudWebClient")                  // 기존 코드 그대로 유지
    public WebClient odcloudWebClient(WebClient.Builder builder) {
        return builder
                .clone()
                .baseUrl("https://api.odcloud.kr")
                .build();
    }

    /* ===== 4) Naver 뉴스 전용 WebClient ===== */
    @Bean
    @Qualifier("naverWebClient")
    public WebClient naverWebClient(
            @Value("${external.naver.client-id}") String clientId,
            @Value("${external.naver.client-secret}") String clientSecret
    ) {
        return WebClient.builder()                  // 새 빌더로 분리
                .baseUrl("https://openapi.naver.com")
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .build();
    }

    /* ===== 5) 한국수출입은행 전용 WebClient ===== */
    @Bean
    @Qualifier("eximWebClient")
    public WebClient eximWebClient(WebClient.Builder builder) {
        return builder
                .clone()
                .baseUrl("https://oapi.koreaexim.go.kr")
                .build();
    }

}
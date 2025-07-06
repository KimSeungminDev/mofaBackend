package com.springboot.api.client;

import com.springboot.api.dto.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    @Value("${exchange.auth-key}")
    private String authKey;

    private final WebClient webClient = createUnsafeWebClient();

    public Mono<ExchangeRateResponse[]> getExchangeRates(String searchDate) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/site/program/financial/exchangeJSON")
                        .queryParam("authkey", authKey)
                        .queryParam("data", "AP01")
                        .queryParam("searchdate", searchDate)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRateResponse[].class);
    }

    private WebClient createUnsafeWebClient() {
        try {
            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> {
                        try {
                            sslContextSpec.sslContext(
                                    SslContextBuilder.forClient()
                                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                            .build()
                            );
                        } catch (SSLException e) {
                            throw new RuntimeException("SSLContext 설정 실패", e);
                        }
                    });

            return WebClient.builder()
                    .baseUrl("https://www.koreaexim.go.kr")
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("WebClient 생성 중 오류 발생", e);
        }
    }
}

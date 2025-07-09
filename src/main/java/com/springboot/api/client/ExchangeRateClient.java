package com.springboot.api.client;

import com.springboot.api.dto.ExchangeRateResponse;
import jakarta.annotation.PostConstruct;
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

    /**
     * API 서버 깨우기용 더미 호출: 서버 기동 시 한 번 호출
     */
    @PostConstruct
    public void initDummyCall() {
        System.out.println("[ExchangeRateClient] 서버 기동 시 더미 API 호출 시작");
        String today = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        getExchangeRates(today).subscribe(
                response -> System.out.println("[ExchangeRateClient] 더미 API 호출 성공, 응답 수: " + response.length),
                error -> System.err.println("[ExchangeRateClient] 더미 API 호출 실패: " + error.getMessage())
        );
    }

    public Mono<ExchangeRateResponse[]> getExchangeRates(String searchDate) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/site/program/financial/exchangeJSON")
                        .queryParam("authkey", authKey)
                        .queryParam("data", "AP01")
                        .queryParam("searchdate", searchDate)
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("API 오류 응답: " + body))))
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

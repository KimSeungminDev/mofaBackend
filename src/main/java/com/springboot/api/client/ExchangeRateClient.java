package com.springboot.api.client;

import com.springboot.api.dto.ExchangeRateResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class ExchangeRateClient {

    @Value("${exchange.auth-key}")
    private String authKey;

    private final WebClient webClient;

    public ExchangeRateClient(@Qualifier("eximWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    public void initDummyCall() {
        System.out.println("[ExchangeRateClient] 🚀 서버 기동 시 더미 API 호출 시작");
        System.out.println("[ExchangeRateClient] authKey: " + authKey);
        System.out.println("[ExchangeRateClient] webClient baseUrl: " + webClient);
        System.out.println(System.getProperty("java.home"));

        String today = LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        String corrected = correctToWeekday(today);
        getExchangeRates(corrected).subscribe(
                response -> System.out.println("[ExchangeRateClient] ✅ 더미 API 응답 수: " + response.length),
                error -> System.err.println("[ExchangeRateClient] ❌ 더미 API 호출 실패: " + error.getMessage())
        );
    }

    private String correctToWeekday(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        DayOfWeek day = date.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY) {
            date = date.minusDays(1);
        } else if (day == DayOfWeek.SUNDAY) {
            date = date.minusDays(2);
        }

        return date.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public Mono<ExchangeRateResponse[]> getExchangeRates(String searchDate) {
        System.out.println("💡 [API 요청] 날짜 = " + searchDate);

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
}
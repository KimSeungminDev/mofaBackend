package com.springboot.api.client;

import com.springboot.api.dto.CountryCurrencyResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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
public class RestCountriesClient {

    private static final String REST_COUNTRIES_BASE_URL = "https://restcountries.com";
    private static final String REST_COUNTRIES_API_PATH = "/v3.1/all?fields=name,currencies,flags";

    private final WebClient webClient = createUnsafeWebClient();

    @PostConstruct
    public void initDummyCall() {
        getAllCountries().subscribe(
                response -> System.out.println("[RestCountriesClient] 더미 API 호출 성공, 국가 수: " + response.length),
                error -> System.err.println("[RestCountriesClient] 더미 API 호출 실패: " + error.getMessage())
        );
    }

    public Mono<CountryCurrencyResponse[]> getAllCountries() {
        return webClient.get()
                .uri(REST_COUNTRIES_API_PATH)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("API 오류: " + body))))
                .bodyToMono(CountryCurrencyResponse[].class);
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
                    .baseUrl(REST_COUNTRIES_BASE_URL)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("WebClient 생성 중 오류 발생", e);
        }
    }
}

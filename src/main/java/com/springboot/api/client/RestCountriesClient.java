package com.springboot.api.client;

import com.springboot.api.dto.CountryCurrencyResponse; // <-- 사용하는 DTO
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

    // 경고: 개발용으로만 사용! 실제 운영 환경에서는 SSL 검증을 활성화해야 합니다.
    // InsecureTrustManagerFactory.INSTANCE 사용은 보안 취약점을 발생시킬 수 있습니다.
    private final WebClient webClient = createUnsafeWebClient();

    // restcountries.com API의 기본 URL
    private static final String REST_COUNTRIES_BASE_URL = "https://restcountries.com";
    // 모든 국가 정보를 가져오며, 필요한 필드(name, currencies, flags)만 요청하는 경로
    private static final String REST_COUNTRIES_API_PATH = "/v3.1/all?fields=name,currencies,flags";

    /**
     * restcountries.com에서 모든 국가 정보를 가져옵니다.
     * 응답에는 국가명, 통화 정보, 국기 URL이 포함됩니다.
     *
     * @return Mono<RestCountriesApiResponse[]> 국가 정보 배열을 포함하는 Mono
     */
    public Mono<CountryCurrencyResponse[]> getAllCountries() {
        return webClient.get()
                .uri(REST_COUNTRIES_BASE_URL + REST_COUNTRIES_API_PATH) // 전체 API URL 구성
                .retrieve() // 응답을 검색 (retrieve)
                .bodyToMono(CountryCurrencyResponse[].class); // 응답 본문을 RestCountriesApiResponse[] 타입으로 변환
    }

    /**
     * 개발 환경에서 HTTPS/SSL 인증서 오류를 무시하도록 설정된 WebClient를 생성합니다.
     * 운영 환경에서는 절대로 사용해서는 안 됩니다.
     *
     * @return SSL 검증을 무시하는 WebClient 인스턴스
     */
    private WebClient createUnsafeWebClient() {
        try {
            // SSLContext를 생성하여 모든 인증서를 신뢰하도록 설정 (보안 취약점)
            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> {
                        try {
                            sslContextSpec.sslContext(
                                    SslContextBuilder.forClient()
                                            .trustManager(InsecureTrustManagerFactory.INSTANCE) // <-- 위험!
                                            .build()
                            );
                        } catch (SSLException e) {
                            throw new RuntimeException("SSLContext 설정 실패", e);
                        }
                    });

            // 생성된 HttpClient를 사용하여 WebClient.Builder를 구성
            return WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient)) // SSL 설정을 적용한 Http 클라이언트 커넥터
                    .build(); // WebClient 인스턴스 빌드

        } catch (Exception e) {
            // WebClient 생성 중 예외 발생 시 런타임 예외 throw
            throw new RuntimeException("WebClient 생성 중 오류 발생", e);
        }
    }
}
// 파일명: ExchangeRateController.java
package com.springboot.api.controller; // 패키지명은 실제 프로젝트에 맞게 변경하세요

import com.springboot.api.dto.CombinedExchangeRateResponse; // <-- DTO명 변경
import com.springboot.api.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api") // 기본 경로 /api
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // CORS 허용 (개발용. 실제 운영에서는 특정 출처만 허용하는 것이 좋음)
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    // 엔드포인트: GET /api/exchange
    @GetMapping("/exchange")
    public Mono<List<CombinedExchangeRateResponse>> getExchangeRates( // <-- 반환 타입 변경
                                                                      @RequestParam(required = false) String searchDate) {

        String targetDate = searchDate;
        if (targetDate == null || targetDate.isEmpty()) {
            targetDate = exchangeRateService.getLatestWeekday(); // 날짜가 없으면 가장 최근 영업일 사용
        }

        return exchangeRateService.getCombinedExchangeRates(targetDate);
    }
}
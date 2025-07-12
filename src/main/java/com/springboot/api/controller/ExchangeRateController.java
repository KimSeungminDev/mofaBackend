// 파일명: ExchangeRateController.java
package com.springboot.api.controller;

import com.springboot.api.dto.CombinedExchangeRateResponse;
import com.springboot.api.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    // GET /api/exchange?searchDate=yyyyMMdd
    @GetMapping("/exchange")
    public Mono<List<CombinedExchangeRateResponse>> getExchangeRates(
            @RequestParam(required = false) String searchDate) {

        String targetDate;

        if (searchDate == null || searchDate.isEmpty()) {
            // 날짜 없으면 오늘 기준 가장 최근 평일 반환
            targetDate = exchangeRateService.getLatestWeekday();
            System.out.println("💡 최종 targetDate = " + targetDate);

        } else {
            // 날짜가 주말이면 평일로 보정
            targetDate = exchangeRateService.correctToWeekday(searchDate);
        }
        System.out.println("💡 최종 targetDate = " + targetDate);

        return exchangeRateService.getCombinedExchangeRates(targetDate);
    }
}

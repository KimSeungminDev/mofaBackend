package com.springboot.api.service;

import com.springboot.api.client.ExchangeRateClient;
import com.springboot.api.client.RestCountriesClient;
import com.springboot.api.dto.CombinedExchangeRateResponse;
import com.springboot.api.dto.CountryCurrencyResponse;
import com.springboot.api.dto.ExchangeRateResponse; // 기존 DTO명 그대로 사용

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateClient exchangeRateClient;
    private final RestCountriesClient restCountriesClient;

    private double parseRateStringToDouble(String rateStr) {
        if (rateStr == null || rateStr.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(rateStr.replace(",", ""));
        } catch (NumberFormatException e) {
            System.err.println("Error parsing rate string: '" + rateStr + "' -> " + e.getMessage());
            return 0.0;
        }
    }

    public Mono<List<CombinedExchangeRateResponse>> getCombinedExchangeRates(String searchDate) {
        Mono<ExchangeRateResponse[]> keximRatesMono = exchangeRateClient.getExchangeRates(searchDate);
        Mono<CountryCurrencyResponse[]> countriesMono = restCountriesClient.getAllCountries();

        return Mono.zip(keximRatesMono, countriesMono)
                .map(tuple -> {
                    ExchangeRateResponse[] keximRates = tuple.getT1();
                    CountryCurrencyResponse[] countries = tuple.getT2();

                    List<CountryCurrencyResponse> countryList = Arrays.asList(countries);
                    List<CombinedExchangeRateResponse> combinedList = new ArrayList<>();

                    if (keximRates == null || keximRates.length == 0) {
                        return combinedList;
                    }

                    for (ExchangeRateResponse keximRate : keximRates) {
                        if (keximRate.getResult() != null && keximRate.getResult() != 1) {
                            System.err.println("KEXIM API item result error: " + keximRate.getResult() + " for " + keximRate.getCurrencyCode());
                            continue;
                        }
                        if (keximRate.getCurrencyCode() == null || keximRate.getCurrencyName() == null || keximRate.getBaseRate() == null) {
                            System.err.println("Incomplete KEXIM API response item, skipping: " + keximRate);
                            continue;
                        }

                        // === 핵심 수정 부분: 통화 코드 정제 ===
                        String originalCurrencyCode = keximRate.getCurrencyCode();
                        String processedCurrencyCode = originalCurrencyCode;
                        if (originalCurrencyCode != null && originalCurrencyCode.contains("(")) {
                            processedCurrencyCode = originalCurrencyCode.substring(0, originalCurrencyCode.indexOf("("));
                        }
                        processedCurrencyCode = processedCurrencyCode.trim(); // 공백 제거 (예: "JPY " -> "JPY")
                        // ===================================

                        final String currentCurrencyCode = processedCurrencyCode; // 매핑에 사용할 정제된 통화 코드

                        List<CountryCurrencyResponse> matchingCountries = countryList.stream()
                                .filter(country -> country.getCurrencies() != null && country.getCurrencies().containsKey(currentCurrencyCode))
                                .collect(Collectors.toList());

                        if (!matchingCountries.isEmpty()) {
                            for (CountryCurrencyResponse country : matchingCountries) {
                                CombinedExchangeRateResponse dto = new CombinedExchangeRateResponse();
                                dto.setCurrencyCode(currentCurrencyCode); // 정제된 코드 저장
                                dto.setCurrencyName(keximRate.getCurrencyName());
                                dto.setBaseRate(parseRateStringToDouble(keximRate.getBaseRate()));
                                dto.setBuyingRate(parseRateStringToDouble(keximRate.getBuyingRate()));
                                dto.setSellingRate(parseRateStringToDouble(keximRate.getSellingRate()));
                                dto.setChangeRate(null);

                                dto.setFlagUrl(country.getFlags() != null ? country.getFlags().getPng() : null);
                                dto.setCountryName(country.getName() != null ? country.getName().getCommon() : null);

                                combinedList.add(dto);
                            }
                        } else {
                            CombinedExchangeRateResponse dto = new CombinedExchangeRateResponse();
                            dto.setCurrencyCode(currentCurrencyCode); // 정제된 코드 저장
                            dto.setCurrencyName(keximRate.getCurrencyName());
                            dto.setBaseRate(parseRateStringToDouble(keximRate.getBaseRate()));
                            dto.setBuyingRate(parseRateStringToDouble(keximRate.getBuyingRate()));
                            dto.setSellingRate(parseRateStringToDouble(keximRate.getSellingRate()));
                            dto.setChangeRate(null);
                            dto.setFlagUrl(null);
                            dto.setCountryName("알 수 없음");
                            combinedList.add(dto);
                            System.out.println("No matching country found for currency code: " + currentCurrencyCode + ". Added as '알 수 없음'.");
                        }
                    }
                    return combinedList;
                });
    }

    // 날짜 없으면 오늘 기준 가장 최근 평일 반환
    public String getLatestWeekday() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        LocalDate targetDate = today;

        if (dayOfWeek == DayOfWeek.SUNDAY) {
            targetDate = today.minusDays(2);
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            targetDate = today.minusDays(1);
        }

        return targetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
    // 날짜가 주말이면 평일로 보정
    public String correctToWeekday(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        DayOfWeek day = date.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY) {
            date = date.minusDays(1);
        } else if (day == DayOfWeek.SUNDAY) {
            date = date.minusDays(2);
        }

        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }


}
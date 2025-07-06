package com.springboot.api.dto;

import lombok.Data;
import java.util.Map;

// restcountries.com API의 응답 구조에 맞춘 DTO
@Data
public class CountryCurrencyResponse { // 새로 만든 DTO
    private Name name;
    private Map<String, Currency> currencies; // { "USD": { "name": "US Dollar", "symbol": "$" } }
    private Flags flags;

    @Data
    public static class Name {
        private String common; // 일반적인 국가명 (예: "United States")
        private String official;
    }

    @Data
    public static class Currency {
        private String name; // 통화명 (예: "US Dollar")
        private String symbol; // 통화 기호 (예: "$")
    }

    @Data
    public static class Flags {
        private String png; // PNG 국기 URL
        private String svg; // SVG 국기 URL
        private String alt; // 대체 텍스트
    }
}
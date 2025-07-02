// CountryPoliticsResponse.java - 외교부_국가·지역별 정치정보 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CountryPoliticsResponse {

    @JsonProperty("currentCount")
    private int currentCount;

    @JsonProperty("matchCount")
    private int matchCount;

    @JsonProperty("page")
    private int page;

    @JsonProperty("perPage")
    private int perPage;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("data")
    private List<CountryPoliticsItem> data;

    @Data
    public static class CountryPoliticsItem {

        @JsonProperty("국가코드(ISO 2자리)")
        private String countryIso;

        @JsonProperty("한글 국가명")
        private String countryNameKo;

        @JsonProperty("영문 국가명")
        private String countryNameEn;

        @JsonProperty("정부형태")
        private String governmentType;

        @JsonProperty("국가형태")
        private String nationalForm;

        @JsonProperty("대외정책")
        private String foreignPolicy;

        @JsonProperty("주요인사")
        private String keyFigures;

        @JsonProperty("주요정당")
        private String majorParties;
    }
}

// CountrySituationResponse.java - 외교부_국가·지역별 주요정세 정보 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CountrySituationResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("body")
        private Body body;
    }

    @Data
    public static class Body {
        @JsonProperty("dataType")
        private String dataType;

        @JsonProperty("items")
        private Items items;

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("pageNo")
        private int pageNo;

        @JsonProperty("totalCount")
        private int totalCount;
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<CountrySituationItem> item;
    }

    @Data
    public static class CountrySituationItem {
        @JsonProperty("country_eng_nm")
        private String countryEngName;

        @JsonProperty("country_nm")
        private String countryName;

        @JsonProperty("country_iso_alp2")
        private String countryIsoCode;

        @JsonProperty("situation_info_cn")
        private String situationInfo;

        @JsonProperty("year")
        private Integer year;

        @JsonProperty("month")
        private Integer month;

        @JsonProperty("day")
        private Integer day;
    }
}

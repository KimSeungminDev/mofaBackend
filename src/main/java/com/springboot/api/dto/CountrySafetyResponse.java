// CountrySafetyResponse.java - 외교부_국가∙지역별 안전공지 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CountrySafetyResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("body")
        private Body body;
    }

    @Data
    public static class Body {
        @JsonProperty("items")
        private Items items;
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<CountrySafetyItem> item;
    }

    @Data
    public static class CountrySafetyItem {

        @JsonProperty("alarm_lvl")
        private String alarmLevel;

        @JsonProperty("continent_cd")
        private String continentCode;

        @JsonProperty("continent_eng_nm")
        private String continentEnglishName;

        @JsonProperty("continent_nm")
        private String continentKoreanName;

        @JsonProperty("country_eng_nm")
        private String countryEnglishName;

        @JsonProperty("country_iso_alp2")
        private String countryIsoAlpha2;

        @JsonProperty("country_nm")
        private String countryKoreanName;

        @JsonProperty("dang_map_download_url")
        private String dangerMapUrl;

        @JsonProperty("flag_download_url")
        private String flagImageUrl;

        @JsonProperty("map_download_url")
        private String mapImageUrl;

        @JsonProperty("org_country_idx")
        private String countryIndex;

        @JsonProperty("region_ty")
        private String regionType;

        @JsonProperty("remark")
        private String remark;

        @JsonProperty("written_dt")
        private String writtenDate;
    }
}

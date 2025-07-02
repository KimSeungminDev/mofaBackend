// TravelAlarmServiceResponse.java - 외교부_국가·지역별 여행경보정보 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TravelAlarmServiceResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {

        @JsonProperty("body")
        private Body body;

        @JsonProperty("header")
        private Header header;
    }

    @Data
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
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
        private List<Item> item;
    }

    @Data
    public static class Item {
        @JsonProperty("alarm_lvl")
        private String alarmLevel;

        @JsonProperty("continent_cd")
        private String continentCode;

        @JsonProperty("continent_eng_nm")
        private String continentEnglish;

        @JsonProperty("continent_nm")
        private String continentKorean;

        @JsonProperty("country_eng_nm")
        private String countryEnglish;

        @JsonProperty("country_iso_alp2")
        private String countryIsoAlpha2;

        @JsonProperty("country_nm")
        private String countryKorean;

        @JsonProperty("dang_map_download_url")
        private String dangerMapUrl;

        @JsonProperty("flag_download_url")
        private String flagUrl;

        @JsonProperty("map_download_url")
        private String mapUrl;

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

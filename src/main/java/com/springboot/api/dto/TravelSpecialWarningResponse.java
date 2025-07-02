// TravelSpecialWarningResponse.java - 외교부_특별여행 정보제도 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TravelSpecialWarningResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {

        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
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
        private List<TravelSpecialWarningItem> item;
    }

    @Data
    public static class TravelSpecialWarningItem {

        @JsonProperty("continent")
        private String continent;

        @JsonProperty("country_en_name")
        private String countryEnglishName;

        @JsonProperty("country_name")
        private String countryKoreanName;

        @JsonProperty("id")
        private String id;

        @JsonProperty("img_url")
        private String flagImageUrl;

        @JsonProperty("img_url_2")
        private String alternateFlagImageUrl;

        @JsonProperty("iso_code")
        private String isoCode;

        @JsonProperty("spban_note")
        private String specialBanNote;

        @JsonProperty("spban_yn_partial")
        private String partialSpecialBanYn;

        @JsonProperty("spban_yna")
        private String fullSpecialBanYn;

        @JsonProperty("splimit")
        private String travelRestriction;

        @JsonProperty("splimit_note")
        private String travelRestrictionNote;

        @JsonProperty("splimit_partial")
        private String travelRestrictionPartial;

        @JsonProperty("wrt_dt")
        private String writtenDate;
    }
}


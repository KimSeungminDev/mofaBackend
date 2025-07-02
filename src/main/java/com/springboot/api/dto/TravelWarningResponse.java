// TravelWarningResponse.java - 외교부_여행경보제도 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TravelWarningResponse {

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
        private List<TravelWarningItem> item;
    }

    @Data
    public static class TravelWarningItem {

        @JsonProperty("attention")
        private String attention;

        @JsonProperty("attention_note")
        private String attentionNote;

        @JsonProperty("attention_partial")
        private String attentionPartial;

        @JsonProperty("ban_note")
        private String banNote;

        @JsonProperty("ban_yn_partial")
        private String banYnPartial;

        @JsonProperty("ban_yna")
        private String banYna;

        @JsonProperty("continent")
        private String continent;

        @JsonProperty("control")
        private String control;

        @JsonProperty("control_note")
        private String controlNote;

        @JsonProperty("control_partial")
        private String controlPartial;

        @JsonProperty("country_en_name")
        private String countryEnName;

        @JsonProperty("country_name")
        private String countryName;

        @JsonProperty("id")
        private String id;

        @JsonProperty("img_url")
        private String imgUrl;

        @JsonProperty("img_url_2")
        private String imgUrl2;

        @JsonProperty("iso_code")
        private String isoCode;

        @JsonProperty("limita")
        private String limitA;

        @JsonProperty("limita_note")
        private String limitANote;

        @JsonProperty("limita_partial")
        private String limitAPartial;

        @JsonProperty("wrt_dt")
        private String writtenDate;
    }
}

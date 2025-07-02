// CountryFlagResponse.java - 외교부_국가∙지역별 국기 이미지 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CountryFlagResponse {

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
        private List<FlagItem> item;
    }

    @Data
    public static class FlagItem {
        @JsonProperty("content_ty")
        private String contentTy;

        @JsonProperty("country_eng_nm")
        private String countryEngNm;

        @JsonProperty("country_iso_alp2")
        private String countryIsoAlp2;

        @JsonProperty("country_nm")
        private String countryNm;

        @JsonProperty("download_url")
        private String downloadUrl;

        @JsonProperty("origin_file_nm")
        private String originFileNm;
    }
}

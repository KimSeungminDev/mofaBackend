package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecialWarningServiceResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @Setter
    public static class Response {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
    }

    @Getter
    @Setter
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
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

    @Getter
    @Setter
    public static class Items {
        @JsonProperty("item")
        private List<SpecialTravelWarningItem> item;
    }

    @Getter
    @Setter
    public static class SpecialTravelWarningItem {
        /** 대륙 코드 (예: 50은 아프리카) */
        @JsonProperty("continent_cd")
        private String continentCd;

        /** 대륙 영문명 (예: Africa) */
        @JsonProperty("continent_eng_nm")
        private String continentEngNm;

        /** 대륙 한글명 (예: 아프리카) */
        @JsonProperty("continent_nm")
        private String continentNm;

        /** 국가 영문명 (예: Ghana) */
        @JsonProperty("country_eng_nm")
        private String countryEngNm;

        /** 국가 한글명 (예: 가나) */
        @JsonProperty("country_nm")
        private String countryNm;

        /** 국가 ISO 알파-2 코드 (예: GH) */
        @JsonProperty("country_iso_alp2")
        private String countryIsoAlp2;

        /** 위험 지도 이미지 다운로드 URL */
        @JsonProperty("dang_map_download_url")
        private String dangMapDownloadUrl;

        /** 여행 금지 권고 지역 비고 (nullable) */
        @JsonProperty("forbidden_rcmnd_remark")
        private String forbiddenRcmndRemark;

        /** 여행 금지 권고 지역 유형 (nullable) */
        @JsonProperty("forbidden_region_ty")
        private String forbiddenRegionTy;

        /** 대피 권고 지역 비고 (nullable) */
        @JsonProperty("evacuate_rcmnd_remark")
        private String evacuateRcmndRemark;

        /** 대피 권고 지역 유형 (nullable) */
        @JsonProperty("evacuate_region_ty")
        private String evacuateRegionTy;

        /** 국가 지도 이미지 다운로드 URL */
        @JsonProperty("map_download_url")
        private String mapDownloadUrl;

        /** 국가 깃발 이미지 다운로드 URL */
        @JsonProperty("flag_download_url")
        private String flagDownloadUrl;

        /** 정보 작성일 (예: 2022-08-02) */
        @JsonProperty("written_dt")
        private String writtenDt;
    }
}

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 외교부_국가·지역별 입국허가요건 API 응답 DTO
 * 제공항목: 여권/사증 필요 여부 및 조건, 무비자 입국 근거, 비고 등
 */
@Data
public class EntranceVisaResponse {

    @JsonProperty("response")
    private ResponseWrapper response;

    @Data
    public static class ResponseWrapper {

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
        private List<Item> item;
    }

    @Data
    public static class Item {
        @JsonProperty("id")
        private int id; // 입국허가요건 ID

        @JsonProperty("country_eng_nm")
        private String countryEngNm; // 국가영문명

        @JsonProperty("country_nm")
        private String countryNm; // 국가명 (한글)

        @JsonProperty("country_iso_alp2")
        private String isoCode; // ISO 2자리 코드

        @JsonProperty("iso_num")
        private String isoNum; // ISO 숫자코드

        @JsonProperty("have_yn")
        private String haveYn; // 여권소지 여부 (Y/N)

        @JsonProperty("gnrl_pspt_visa_yn")
        private String generalPassportVisaYn; // 일반여권 사증 필요 여부 (Y/N)

        @JsonProperty("gnrl_pspt_visa_cn")
        private String generalPassportVisaCn; // 일반여권 사증 조건 내용

        @JsonProperty("ofclpspt_visa_yn")
        private String officialPassportVisaYn; // 관용여권 사증 필요 여부 (Y/N)

        @JsonProperty("ofclpspt_visa_cn")
        private String officialPassportVisaCn; // 관용여권 사증 조건 내용

        @JsonProperty("dplmt_pspt_visa_yn")
        private String diplomaticPassportVisaYn; // 외교관여권 사증 필요 여부 (Y/N)

        @JsonProperty("dplmt_pspt_visa_cn")
        private String diplomaticPassportVisaCn; // 외교관여권 사증 조건 내용

        @JsonProperty("nvisa_entry_evdc_cn")
        private String nvisaEntryEvdcCn; // 무비자 입국 근거 내용

        @JsonProperty("remark")
        private String remark; // 비고
    }
}

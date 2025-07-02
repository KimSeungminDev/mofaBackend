// EntranceVisaResponse.java - 외교부_국가·지역별 입국허가요건 API 응답 DTO

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

    /** 결과 코드 ("00" 정상 등) */
    @JsonProperty("resultCode")
    private String resultCode;

    /** 결과 메시지 ("정상") */
    @JsonProperty("resultMsg")
    private String resultMsg;

    /** 한 페이지 결과 수 */
    @JsonProperty("numOfRows")
    private int numOfRows;

    /** 페이지 번호 */
    @JsonProperty("pageNo")
    private int pageNo;

    /** 전체 결과 수 */
    @JsonProperty("totalCount")
    private int totalCount;

    /** 현재 결과 수 */
    @JsonProperty("currentCount")
    private int currentCount;

    /** 입국허가요건 목록 */
    @JsonProperty("data")
    private DataItems data;

    @Data
    public static class DataItems {
        @JsonProperty("item")
        private List<Item> items;
    }

    @Data
    public static class Item {
        @JsonProperty("Id")
        private String id; // 입국허가요건 ID

        @JsonProperty("country_eng_nm")
        private String countryEngNm; // 국가영문명

        @JsonProperty("country_nm")
        private String countryNm; // 국가명 (한글)

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
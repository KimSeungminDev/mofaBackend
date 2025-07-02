package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 외교부_국제경제동향 API 응답 DTO
 * 제공항목: 국가별 경제지표(예: GDP, 환율, 물가 등) 관련 보고서 정보
 */
@Data
public class GlobalEconomicTrendsResponse {

    @JsonProperty("response")
    private ApiResponse response;

    @Data
    public static class ApiResponse {
        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;
    }

    @Data
    public static class Header {
        /** 결과 코드 ("0" = 정상) */
        @JsonProperty("resultCode")
        private String resultCode;

        /** 결과 메시지 ("정상") */
        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Data
    public static class Body {
        /** 요청한 데이터 형식 (예: JSON) */
        @JsonProperty("dataType")
        private String dataType;

        /** 현재 페이지 번호 */
        @JsonProperty("pageNo")
        private int pageNo;

        /** 페이지 당 항목 수 */
        @JsonProperty("numOfRows")
        private int numOfRows;

        /** 전체 데이터 수 */
        @JsonProperty("totalCount")
        private int totalCount;

        /** 실제 데이터 항목 */
        @JsonProperty("items")
        private Items items;
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<GlobalEconomicItem> item;
    }

    @Data
    public static class GlobalEconomicItem {
        /** 문서 제목 */
        @JsonProperty("title")
        private String title;

        /** 문서 내용 (HTML 태그 포함) */
        @JsonProperty("content")
        private String content;

        /** 업데이트 날짜 (yyyy-MM-dd) */
        @JsonProperty("updt_date")
        private String updateDate;

        /** 첨부 파일 다운로드 URL */
        @JsonProperty("file_url")
        private String fileUrl;

        /** 작성자 (nullable) */
        @JsonProperty("creator")
        private String creator;
    }
}

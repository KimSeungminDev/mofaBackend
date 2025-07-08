package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverNewsResponse {

    //  예시: "Tue, 08 Jul 2025 11:24:03 +0900"
    private String lastBuildDate;
    private int total;      // 전체 검색 건수
    private int start;      // 요청 시작 위치
    private int display;    // 한 번에 내려받은 건수
    private List<Item> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String title;        // HTML <b> 태그 포함
        private String originallink; // 언론사 원문 링크
        private String link;         // 네이버 뉴스 링크
        private String description;  // 요약 (태그 포함)
        private String pubDate;      // "Tue, 08 Jul 2025 10:55:00 +0900"\

        private String imageUrl;     // 추가 생성한 필드(뉴스 사진 기록용)
    }
}

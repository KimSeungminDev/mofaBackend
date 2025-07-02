// ConsularFaqResponse.java - 외교부_해외안전여행홈페이지 자주묻는 질문 목록 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ConsularFaqResponse {

    @JsonProperty("currentCount")
    private int currentCount;

    @JsonProperty("matchCount")
    private int matchCount;

    @JsonProperty("page")
    private int page;

    @JsonProperty("perPage")
    private int perPage;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("data")
    private List<FaqItem> data;

    @Data
    public static class FaqItem {

        @JsonProperty("답변")
        private String answer;

        @JsonProperty("등록일")
        private String registerDate;

        @JsonProperty("순번")
        private String id;

        @JsonProperty("제목(질문)")
        private String question;
    }
}

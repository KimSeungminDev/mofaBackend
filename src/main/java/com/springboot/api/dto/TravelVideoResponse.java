// TravelVideoResponse.java - 외교부_해외안전여행홈페이지 영상 링크 목록 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TravelVideoResponse {

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
    private List<TravelVideoData> data;

    @Data
    public static class TravelVideoData {

        @JsonProperty("내용")
        private String content;

        @JsonProperty("등록일")
        private String registerDate;

        @JsonProperty("순번")
        private int index;

        @JsonProperty("영상링크")
        private String videoLink;

        @JsonProperty("영상제목")
        private String videoTitle;
    }
}

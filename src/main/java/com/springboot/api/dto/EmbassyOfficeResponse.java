// EmbassyOfficeResponse.java - 외교부_국가·지역별 재외공관 정보 API 응답 DTO

package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmbassyOfficeResponse {

    @JsonProperty("page")
    private int page;

    @JsonProperty("perPage")
    private int perPage;

    @JsonProperty("totalCount")
    private int totalCount;

    @JsonProperty("currentCount")
    private int currentCount;

    @JsonProperty("matchCount")
    private int matchCount;

    @JsonProperty("data")
    private List<EmbassyOfficeData> data;

    @Data
    public static class EmbassyOfficeData {
        @JsonProperty("국가명")
        private String countryName;

        @JsonProperty("국가코드(ISO 2자리 코드)")
        private String isoCode;

        @JsonProperty("지역(대륙)")
        private String continent;

        @JsonProperty("재외공관명")
        private String embassyName;

        @JsonProperty("재외공관 종류")
        private String embassyType;

        @JsonProperty("재외공관 주소")
        private String address;

        @JsonProperty("재외공관 전화번호")
        private String phoneNumber;

        @JsonProperty("재외공관 팩스번호")
        private String faxNumber;

        @JsonProperty("긴급전화번호")
        private String emergencyPhone;

        @JsonProperty("공관홈페이지")
        private String homepage;
    }
}

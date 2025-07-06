package com.springboot.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExchangeRateResponse {

    @JsonProperty("result")
    private Integer result; // 1: 성공, 2: DATA코드 오류, 3: 인증코드 오류, 4: 일일 제한 횟수 초과

    @JsonProperty("cur_unit")
    private String currencyCode;

    @JsonProperty("cur_nm")
    private String currencyName;

    @JsonProperty("ttb")
    private String buyingRate; // 전신환(송금 받을 때)

    @JsonProperty("tts")
    private String sellingRate; // 전신환(송금 보낼 때)

    @JsonProperty("deal_bas_r")
    private String baseRate; // 매매 기준율

    @JsonProperty("bkpr")
    private String bkpr; // 장부가격

    @JsonProperty("yy_efee_r")
    private String yearEfeeRate; // 년환가료율

    @JsonProperty("ten_dd_efee_r")
    private String tenDayEfeeRate; // 10일환가료율

    @JsonProperty("kftc_deal_bas_r")
    private String kftcBaseRate; // 서울외국환중개 매매기준율

    @JsonProperty("kftc_bkpr")
    private String kftcBkpr; // 서울외국환중개 장부가격
}

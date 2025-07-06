package com.springboot.api.dto;

import lombok.Data;

@Data
public class CombinedExchangeRateResponse {
    private String currencyCode;
    private String currencyName;
    private double baseRate;
    private double buyingRate;
    private double sellingRate;
    private Double changeRate;
    private String flagUrl;
    private String countryName;
}
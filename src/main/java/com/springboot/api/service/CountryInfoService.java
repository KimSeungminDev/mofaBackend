package com.springboot.api.service;

import com.springboot.api.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CountryInfoService {

    private final ConsularFaqClient consularFaqClient;
    private final CountryFlagClient countryFlagClient;
    private final CountryPoliticsClient countryPoliticsClient;
    private final CountrySafetyClient countrySafetyClient;
    private final CountrySituationClient countrySituationClient;
    private final EmbassyOfficeClient embassyOfficeClient;
    private final EntranceVisaClient entranceVisaClient;
    private final GlobalEconomicTrendsClient globalEconomicTrendsClient;
    private final SpecialWarningServiceClient specialWarningServiceClient;
    private final TravelAlarmServiceClient travelAlarmServiceClient;
    private final TravelSpecialWarningClient travelSpecialWarningClient;
    private final TravelVideoClient travelVideoClient;
    private final TravelWarningClient travelWarningClient;

    public Mono<Map<String, Object>> getCountryInfo(String countryName) {

        List<Mono<?>> monoList = List.of(
                consularFaqClient.fetchFaqs(1, 10),
                countryFlagClient.fetchCountryFlag(countryName),
                countryPoliticsClient.fetchCountryPolitics(countryName),
                countrySafetyClient.fetchCountrySafety(countryName),
                countrySituationClient.fetchCountrySituation(countryName),
                embassyOfficeClient.fetchEmbassyOffices(countryName),
                entranceVisaClient.fetchEntranceVisa(countryName),
                globalEconomicTrendsClient.fetchGlobalEconomicTrends(1, 10),
                specialWarningServiceClient.fetchSpecialWarnings(countryName),
                travelAlarmServiceClient.fetchAlarmInfo(countryName),
                travelSpecialWarningClient.fetchSpecialWarnings(countryName),
                travelVideoClient.fetchTravelVideo(1, 10),
                travelWarningClient.fetchTravelWarnings(countryName)
        );

        return Mono.zip(monoList, results -> {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("consularFaqClient", results[0]);
            resultMap.put("countryFlagClient", results[1]);
            resultMap.put("countryPoliticsClient", results[2]);
            resultMap.put("countrySafetyClient", results[3]);
            resultMap.put("countrySituationClient", results[4]);
            resultMap.put("embassyOfficeClient", results[5]);
            resultMap.put("entranceVisaClient", results[6]);
            resultMap.put("globalEconomicTrendsClient", results[7]);
            resultMap.put("specialWarningServiceClient", results[8]);
            resultMap.put("travelAlarmServiceClient", results[9]);
            resultMap.put("travelSpecialWarningClient", results[10]);
            resultMap.put("travelVideoClient", results[11]);
            resultMap.put("travelWarningClient", results[12]);
            return resultMap;
        });
    }
}

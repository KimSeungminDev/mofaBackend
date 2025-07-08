package com.springboot.api.service;

import com.springboot.api.client.*;
import com.springboot.api.dto.NaverNewsResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final NaverNewsClient naverNewsClient;

    public Mono<Map<String, Object>> getCountryInfo(String countryName) {

        Mono<List<NaverNewsResponse.Item>> newsMono = naverNewsClient.searchNews(countryName + " 외교")
                .map(NaverNewsResponse::getItems)
                .map(this::processNewsItems); // ✅ 태그 제거 + og:image 처리

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
                travelAlarmServiceClient.fetchAlarmInfo(countryName, null),
                travelSpecialWarningClient.fetchSpecialWarnings(countryName),
                travelVideoClient.fetchTravelVideo(1, 10),
                travelWarningClient.fetchTravelWarnings(countryName),
                newsMono // ✅ 수정된 Mono
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
            resultMap.put("naverNewsClient", results[13]); // ✅ 여기엔 이제 List<Item>
            return resultMap;
        });
    }

    // ✅ 뉴스 항목 가공: 태그 제거 + 썸네일 추출
    private List<NaverNewsResponse.Item> processNewsItems(List<NaverNewsResponse.Item> items) {
        return items.stream().map(item -> {
            // 가공을 할 때, 태그 제거를 원하지 않아 타이클과 내용 항목은 태그 제거 로직 주석처리
//            item.setTitle(stripHtml(item.getTitle()));
//            item.setDescription(stripHtml(item.getDescription()));
            item.setImageUrl(extractOgImage(item.getOriginallink()));
            return item;
        }).collect(Collectors.toList());
    }

    private String stripHtml(String input) {
        return input == null ? "" : input.replaceAll("<[^>]*>", "");
    }

    private String extractOgImage(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(3000)
                    .get();

            Element ogImage = doc.selectFirst("meta[property=og:image]");
            return ogImage != null ? ogImage.attr("content") : getDefaultImage();

        } catch (Exception e) {
            return getDefaultImage();
        }
    }

    private String getDefaultImage() {
        return "https://cdn-icons-png.flaticon.com/512/2150/2150342.png";
    }
}

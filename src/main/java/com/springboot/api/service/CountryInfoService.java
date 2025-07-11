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

    public Mono<Map<String, Object>> getCountryInfo(String countryName, int newsCount) {

        // ✅ 일부 클라이언트에서만 사용할 alias 처리 (예: 미국 → 미합중국)
        String aliasName = aliasIfNeeded(countryName);

        // ✅ 네이버 뉴스 Mono 구성
        Mono<List<NaverNewsResponse.Item>> newsMono = naverNewsClient
                .searchNews(countryName + " 외교", newsCount, 1, "sim")
                .map(NaverNewsResponse::getItems)
                .map(this::processNewsItems); // ✅ 태그 제거 + og:image 처리

        // ✅ 클라이언트 요청 리스트
        List<Mono<?>> monoList = List.of(
//              consularFaqClient.fetchFaqs(1, 10),
                countryFlagClient.fetchCountryFlag(aliasName), // ✅ alias 적용
                countryPoliticsClient.fetchCountryPolitics(aliasName), // ✅ alias 적용
                countrySafetyClient.fetchCountrySafety(aliasName), // ✅ alias 적용
                countrySituationClient.fetchCountrySituation(countryName),
                embassyOfficeClient.fetchEmbassyOffices(countryName),
                entranceVisaClient.fetchEntranceVisa(aliasName),
//              globalEconomicTrendsClient.fetchGlobalEconomicTrends(1, 10),
                specialWarningServiceClient.fetchSpecialWarnings(aliasName), // ✅ alias 적용
                travelAlarmServiceClient.fetchAlarmInfo(aliasName, null), // ✅ alias 적용
                travelSpecialWarningClient.fetchSpecialWarnings(countryName),
//              travelVideoClient.fetchTravelVideo(1, 10),
                travelWarningClient.fetchTravelWarnings(countryName),
                newsMono // ✅ 수정된 Mono
        );

        // ✅ 키값 리스트 (위 순서와 1:1 매칭)
        List<String> keys = List.of(
//              "consularFaqClient",
                "countryFlagClient",
                "countryPoliticsClient",
                "countrySafetyClient",
                "countrySituationClient",
                "embassyOfficeClient",
                "entranceVisaClient",
//              "globalEconomicTrendsClient",
                "specialWarningServiceClient",
                "travelAlarmServiceClient",
                "travelSpecialWarningClient",
//              "travelVideoClient",
                "travelWarningClient",
                "naverNewsClient"
        );

        // ✅ 결과 매핑
        return Mono.zip(monoList, results -> {
            Map<String, Object> resultMap = new HashMap<>();
            for (int i = 0; i < results.length && i < keys.size(); i++) {
                resultMap.put(keys.get(i), results[i]);
            }
            return resultMap;
        });
    }

    // ✅ 특정 클라이언트용 국가명 치환 로직
    // 중앙아프리카-중앙아프리카공화국, 교황청-바티칸 해당 두 분류는 애매한 부분 있어서 제외
    private String aliasIfNeeded(String countryName) {
        if ("미국".equals(countryName)) {
            return "미합중국";
        } else if ("네팔".equals(countryName)) {
            return "네팔연방";
        } else if ("키르기스스탄".equals(countryName)) {
            return "키르기즈공화국";
        } else if ("베네수엘라".equals(countryName)) {
            return "베네수엘라볼리바르";
        } else if ("튀르키예".equals(countryName)) {
            return "튀르키예공화국";
        }
        return countryName;
    }

    // ✅ 뉴스 항목 가공: 태그 제거 + 썸네일 추출
    private List<NaverNewsResponse.Item> processNewsItems(List<NaverNewsResponse.Item> items) {
        return items.stream().map(item -> {
            // 가공을 할 때, 태그 제거를 원하지 않아 타이틀과 내용 항목은 태그 제거 로직 주석처리
//          item.setTitle(stripHtml(item.getTitle()));
//          item.setDescription(stripHtml(item.getDescription()));
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

package com.springboot.api.service;

import com.springboot.api.client.NaverNewsClient;
import com.springboot.api.dto.NaverNewsResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsPageService {

    private final NaverNewsClient naverNewsClient;

    public Mono<List<NaverNewsResponse.Item>> getDefaultNews() {
        String defaultQuery = "외교";
        int display = 20;
        int start = 1;
        String sort = "sim";

        return naverNewsClient.searchNews(defaultQuery, display, start, sort)
                .map(NaverNewsResponse::getItems)
                .map(this::processNewsItems);
    }

    private List<NaverNewsResponse.Item> processNewsItems(List<NaverNewsResponse.Item> items) {
        return items.stream()
                .map(item -> {
                    item.setImageUrl(extractOgImage(item.getOriginallink()));
                    return item;
                })
                .collect(Collectors.toList());
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

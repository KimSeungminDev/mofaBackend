package com.springboot.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeVideoResponse {

    // 유튜브 영상 제목
    private String title;

    // 유튜브 영상 게시일 (ISO 8601 형식)
    private String publishedAt;

    // 영상 설명
    private String description;

    // 영상 URL (ex: https://youtu.be/AbC12345678)
    private String videoUrl;

    // 썸네일 이미지 URL
    private String thumbnailUrl;
}

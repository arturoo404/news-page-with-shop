package com.arturoo404.NewsPage.entity.news.photo.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PhotoPropertiesDto {

    private Long photoId;
    private Integer photoPosition;
    private Integer photoWidth;
    private Integer photoHeight;
    private String photoPlace;
}

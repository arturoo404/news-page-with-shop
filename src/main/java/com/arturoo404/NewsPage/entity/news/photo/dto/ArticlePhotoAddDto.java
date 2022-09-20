package com.arturoo404.NewsPage.entity.news.photo.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticlePhotoAddDto {

    private Long articleId;
    private Integer photoPosition;
    private Integer photoWidth;
    private Integer photoHeight;
    private String photoPlace;
}

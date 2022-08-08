package com.arturoo404.NewsPage.entity.photo.dto;
import lombok.Getter;

@Getter
public class ArticlePhotoAddDto {

    private Long articleId;
    private Integer photoPosition;
    private Integer photoWidth;
    private Integer photoHeight;
    private String photoPlace;
}

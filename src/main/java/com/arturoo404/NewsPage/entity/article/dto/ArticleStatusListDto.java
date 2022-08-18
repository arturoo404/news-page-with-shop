package com.arturoo404.NewsPage.entity.article.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArticleStatusListDto {

    private Long articleId;
    private boolean status;
    private String title;
    private String journalist;
}

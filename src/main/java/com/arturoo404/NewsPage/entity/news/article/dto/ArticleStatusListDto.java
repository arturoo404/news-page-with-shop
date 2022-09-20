package com.arturoo404.NewsPage.entity.news.article.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleStatusListDto {

    private Long articleId;
    private boolean status;
    private String title;
    private String journalist;
}

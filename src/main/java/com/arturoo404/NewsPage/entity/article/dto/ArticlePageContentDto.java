package com.arturoo404.NewsPage.entity.article.dto;

import com.arturoo404.NewsPage.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class ArticlePageContentDto {

    private Long contentId;
    private ContentType contentType;
    private String text;
}

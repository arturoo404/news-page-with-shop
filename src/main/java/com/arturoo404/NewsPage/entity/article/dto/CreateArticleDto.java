package com.arturoo404.NewsPage.entity.article.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateArticleDto {

    private String title;
    private String content;
    private List<String> tags;
    private Short journalist;
}

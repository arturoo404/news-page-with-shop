package com.arturoo404.NewsPage.entity.article.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateArticleDto {

    private String title;
    private String content;
    private List<String> tags;
    private Short journalist;
}

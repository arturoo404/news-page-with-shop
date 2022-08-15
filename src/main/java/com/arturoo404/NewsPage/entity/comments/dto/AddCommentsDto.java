package com.arturoo404.NewsPage.entity.comments.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddCommentsDto {

    private String email;
    private String content;
    private Long articleId;
}

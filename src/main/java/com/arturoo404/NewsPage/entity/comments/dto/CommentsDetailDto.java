package com.arturoo404.NewsPage.entity.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CommentsDetailDto {

    private String nick;
    private String content;
    private String date;
}

package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.comments.dto.AddCommentsDto;

import java.text.ParseException;

public interface CommentsService {
    void addComments(AddCommentsDto addCommentsDto) throws ParseException;
}

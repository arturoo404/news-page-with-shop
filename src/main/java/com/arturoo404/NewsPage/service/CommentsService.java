package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

import java.text.ParseException;
import java.util.List;

public interface CommentsService {
    void addComments(AddCommentsDto addCommentsDto) throws Exception;

    List<CommentsDetailDto> getCommentsDetail(Long articleId);
}

package com.arturoo404.NewsPage.service.news;

import com.arturoo404.NewsPage.entity.news.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.news.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

import java.util.List;

public interface CommentsService {
    void addComments(AddCommentsDto addCommentsDto) throws Exception;

    List<CommentsDetailDto> getCommentsDetail(Long articleId);

    void deleteComments(Long id) throws ExistInDatabaseException;
}

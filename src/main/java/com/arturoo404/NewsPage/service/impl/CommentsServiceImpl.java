package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.repository.CommentsRepository;
import com.arturoo404.NewsPage.service.CommentsService;
import org.springframework.stereotype.Service;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }
}

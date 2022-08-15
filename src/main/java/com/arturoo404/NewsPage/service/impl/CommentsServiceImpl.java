package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.entity.comments.Comments;
import com.arturoo404.NewsPage.entity.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.repository.ArticleRepository;
import com.arturoo404.NewsPage.repository.CommentsRepository;
import com.arturoo404.NewsPage.repository.UserRepository;
import com.arturoo404.NewsPage.service.CommentsService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void addComments(AddCommentsDto addCommentsDto) throws ParseException {
        String pattern = "yyyy-MM-dd-HH-mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());

        commentsRepository.save(Comments.builder().article(articleRepository
                 .findArticleById(addCommentsDto.getArticleId()))
                 .content(addCommentsDto.getContent()).user(userRepository.findUserByEmail(addCommentsDto.getEmail()))
                 .date(simpleDateFormat.parse(date))
                 .build());
    }
}

package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.entity.comments.Comments;
import com.arturoo404.NewsPage.entity.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.repository.ArticleRepository;
import com.arturoo404.NewsPage.repository.CommentsRepository;
import com.arturoo404.NewsPage.repository.UserRepository;
import com.arturoo404.NewsPage.service.CommentsService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        commentsRepository.save(Comments.builder().article(articleRepository
                 .findArticleById(addCommentsDto.getArticleId()))
                 .content(addCommentsDto.getContent()).user(userRepository.findUserByEmail(addCommentsDto.getEmail()))
                 .date(parseDate(new Date()))
                 .build());
    }

    @Override
    public List<CommentsDetailDto> getCommentsDetail(Long articleId) {

        return commentsRepository.findAllCommentsByArticleId(articleId).stream()
                .map(c -> {
                    try {
                        return new CommentsDetailDto(c.getUser().getNick(), c.getContent(), parseDate(c.getDate()).toString()
                        );
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private Date parseDate(Date date) throws ParseException {
        String pattern = "yyyy-MM-dd-HH-mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String format = simpleDateFormat.format(date);

        return simpleDateFormat.parse(format);
    }
}

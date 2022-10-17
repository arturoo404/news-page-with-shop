package com.arturoo404.NewsPage.service.impl.news;

import com.arturoo404.NewsPage.entity.news.comments.Comments;
import com.arturoo404.NewsPage.entity.news.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.news.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.news.ArticleRepository;
import com.arturoo404.NewsPage.repository.news.CommentsRepository;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.service.news.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private final CommentsRepository commentsRepository;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ArticleRepository articleRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void addComments(AddCommentsDto addCommentsDto) throws Exception {
        User user = userRepository.findUserByEmail(addCommentsDto.getEmail());
        if (addCommentsDto.getContent().getBytes(StandardCharsets.UTF_8).length < 10){
            throw new Exception("Comments should have length over 10 characters.");
        }
        if (user == null){
            throw new ExistInDatabaseException("User not found in database.");
        }
        commentsRepository.save(Comments.builder().article(articleRepository
                 .findArticleById(addCommentsDto.getArticleId()))
                 .content(addCommentsDto.getContent()).user(user)
                 .date(parseDate(new Date()))
                 .build());
    }

    @Override
    public List<CommentsDetailDto> getCommentsDetail(Long articleId) {

        return commentsRepository.findAllCommentsByArticleIdOrderByIdDesc(articleId).stream()
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

    @Override
    public void deleteComments(Long id) throws ExistInDatabaseException {
        Optional<Comments> comments = commentsRepository.findById(id);
        if (comments.isEmpty()){
            throw new ExistInDatabaseException("Comments not found");
        }

        commentsRepository.delete(comments.get());
    }

    private Date parseDate(Date date) throws ParseException {
        String pattern = "yyyy-MM-dd-HH-mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String format = simpleDateFormat.format(date);

        return simpleDateFormat.parse(format);
    }
}

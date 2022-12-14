package com.arturoo404.NewsPage.service.article;

import com.arturoo404.NewsPage.entity.news.article.Article;
import com.arturoo404.NewsPage.entity.news.comments.Comments;
import com.arturoo404.NewsPage.entity.news.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.news.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.repository.news.ArticleRepository;
import com.arturoo404.NewsPage.repository.news.CommentsRepository;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.service.impl.news.CommentsServiceImpl;
import com.arturoo404.NewsPage.service.news.CommentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentsServiceTest {

    private CommentsService commentsService;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        commentsService = new CommentsServiceImpl(commentsRepository, userRepository, articleRepository);
    }

    @Test
    void itShouldThrowLengthException() {
        //Given
        AddCommentsDto addCommentsDto = new AddCommentsDto("email@gmail.com", "Test", 1L);

        //When
        try {
            commentsService.addComments(addCommentsDto);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Comments should have length over 10 characters.");
        }
    }

    @Test
    void itShouldThrowExistInDataBaseException() {
        //Given
        AddCommentsDto addCommentsDto = new AddCommentsDto("email@gmail.com", "Test length over 10", 1L);

        //When
        try {
            commentsService.addComments(addCommentsDto);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("User not found in database.");
        }
    }

    @Test
    void getCommentsDetail() {
        //Given
        User user = User.builder()
                .nick("nick")
                .email("email@email.com")
                .build();

        Article article = Article.builder()
                .id(1L)
                .build();

        Comments comments = Comments.builder()
                .user(user)
                .article(article)
                .date(new Date())
                .content("Test content to test")
                .build();

        //When
        when(commentsRepository.findAllCommentsByArticleIdOrderByIdDesc(1L)).thenReturn(List.of(comments));
        final List<CommentsDetailDto> commentsDetail = commentsService.getCommentsDetail(1L);

        //Then
        assertThat(commentsDetail.size()).isNotZero();
        assertThat(commentsDetail.get(0).getContent()).isNotBlank();
        assertThat(commentsDetail.get(0).getNick()).isNotBlank();
    }
}
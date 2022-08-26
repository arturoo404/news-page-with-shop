package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.comments.Comments;
import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentsRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private JournalistRepository journalistRepository;

    @Test
    void itShouldFindAllCommentsByArticleIdOrderByIdDesc() {

        //Given
        Article article = articleRepository.save(Article.builder()
                .id(1L)
                .title("Title")
                .journalist(journalistRepository.save(
                        Journalist.builder()
                                .name("Journalist Test")
                                .info("Test info")
                                .build()))
                .build());

        commentsRepository.save(Comments.builder()
                        .article(article)
                        .content("Test content")
                        .date(Date.from(Instant.now()))
                .build());

        //When
         List<Comments> allCommentsByArticleIdOrderByIdDesc = commentsRepository.findAllCommentsByArticleIdOrderByIdDesc(1L);

         //Then
        assertThat(allCommentsByArticleIdOrderByIdDesc.size()).isGreaterThan(0);
        assertThat(allCommentsByArticleIdOrderByIdDesc.get(0).getContent()).isNotBlank();
        assertThat(allCommentsByArticleIdOrderByIdDesc.get(0).getArticle().getId()).isEqualTo(article.getId());
    }
}
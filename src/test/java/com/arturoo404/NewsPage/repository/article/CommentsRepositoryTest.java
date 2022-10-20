package com.arturoo404.NewsPage.repository.article;

import com.arturoo404.NewsPage.entity.news.article.Article;
import com.arturoo404.NewsPage.entity.news.comments.Comments;
import com.arturoo404.NewsPage.entity.news.journalist.Journalist;
import com.arturoo404.NewsPage.repository.news.ArticleRepository;
import com.arturoo404.NewsPage.repository.news.CommentsRepository;
import com.arturoo404.NewsPage.repository.news.JournalistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        commentsRepository.saveAll(
                List.of(Comments.builder()
                        .article(article)
                        .content("Test content")
                        .date(Date.from(Instant.now()))
                        .build(),
                        Comments.builder()
                                .article(article)
                                .content("Test content")
                                .date(Date.from(Instant.now()))
                                .build())
        );

        //When
         List<Comments> allCommentsByArticleIdOrderByIdDesc = commentsRepository.findAllCommentsByArticleIdOrderByIdDesc(article.getId());

         //Then
        assertThat(allCommentsByArticleIdOrderByIdDesc.size()).isNotZero();
        assertThat(allCommentsByArticleIdOrderByIdDesc.get(0).getContent()).isNotBlank();
        assertThat(allCommentsByArticleIdOrderByIdDesc.get(0).getArticle().getId()).isEqualTo(article.getId());
    }
}
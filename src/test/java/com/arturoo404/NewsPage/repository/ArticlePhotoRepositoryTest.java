package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.content.Content;
import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.photo.ArticlePhoto;
import com.arturoo404.NewsPage.enums.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ArticlePhotoRepositoryTest {

    @Autowired
    private ArticlePhotoRepository articlePhotoRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private JournalistRepository journalistRepository;

    @BeforeEach
    void setUp() {
       Article article = Article.builder().journalist(
                        journalistRepository.save(
                                Journalist.builder()
                                .name("Journalist Test")
                                .info("Test info")
                                .build())
                )
                .title("Title")
                .build();
        article.setContent(List.of(new Content(ContentType.PHOTO, new ArticlePhoto(1), article)));

        articleRepository.save(article);
    }

    @Test
    void isShouldFindByArticleIdAndPosition() {
        //Given
        Article article = articleRepository.findArticleById(1L);

        //When
        ArticlePhoto byArticleIdAndPosition = articlePhotoRepository
                .findByArticleIdAndPosition(1L, 1);

        //Then
        assertThat(byArticleIdAndPosition.getPhotoPosition()).isEqualTo(1);
        assertThat(article.getContent().get(0).getContentType()).isEqualTo(ContentType.PHOTO);
        assertThat(article.getContent().get(0).getArticlePhoto().getPhotoPosition()).isEqualTo(1);
    }
}
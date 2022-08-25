package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.journalist.Journalist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private JournalistRepository journalistRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("Should return article find by id.")
    void isShouldFindArticleById() {
        
        //Given
         Article article = articleRepository.save(Article.builder()
                 .journalist(journalistRepository.save(Journalist.builder()
                         .name("Journalist Test")
                         .info("Test info")
                         .build()))
                 .title("Title")
                 .build());

        //When
        Article articleFromDataBase = articleRepository.findArticleById(article.getId());

        System.out.println(articleFromDataBase.getId());
        //Then
        assertThat(articleFromDataBase.getTitle().equals(article.getTitle()));
        assertThat(articleFromDataBase.getJournalist().getId().equals(article.getJournalist().getId()));
    }
}
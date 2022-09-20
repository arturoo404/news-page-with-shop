package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.news.article.Article;
import com.arturoo404.NewsPage.entity.news.journalist.Journalist;
import com.arturoo404.NewsPage.repository.news.ArticleRepository;
import com.arturoo404.NewsPage.repository.news.JournalistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private JournalistRepository journalistRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("Should return article find by id.")
    void itShouldFindArticleById() {
        //Given
         Article article = articleRepository.save(getBuild());

        //When
        Article articleFromDataBase = articleRepository.findArticleById(article.getId());

        //Then
        assertThat(articleFromDataBase.getTitle().equals(article.getTitle())).isTrue();
        assertThat(articleFromDataBase.getJournalist().getId().equals(article.getJournalist().getId())).isTrue();
    }

    @Test
    void itShouldFindPageOfArticleByKeyword() {
        //Given
        articleRepository.save(getBuild());

        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        //When
        Page<Article> byKeyword = articleRepository.findAllByKeyword("it", pageable);

        //Then
        assertThat(byKeyword.getTotalPages()).isNotZero();
    }

    @Test
    void itShouldUpdateArticlePopularity() {
        //Given
        Article article = articleRepository.save(getBuild());

        //When
        articleRepository.updatePopularityRanking(article.getId(), article.getArticlePopularity() + 1);

        Article articleFromDb = articleRepository.findArticleById(article.getId());

        //Then
        assertThat(article.getArticlePopularity()).isNotEqualTo(articleFromDb.getArticlePopularity());
    }
    @Test
    void itShouldFindArticleById3() {
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

        //Then
        assertThat(articleFromDataBase.getTitle().equals(article.getTitle())).isTrue();
        assertThat(articleFromDataBase.getJournalist().getId().equals(article.getJournalist().getId())).isTrue();
    }
    @Test
    void itShouldReturnListOfLastPublishedArticles() {
        //Given
        List<Article> articles = getArticles();
        final List<Article> articleList = articleRepository.saveAll(articles);

        //When
        List<Article> articleFromDataBase = articleRepository.findArticleLastPublishedList();

        //Then
        assertThat(articleFromDataBase.stream()
                .filter(a -> articleList.get(0).getId().equals(a.getId()))
                .count()).isZero();
        assertThat(articleFromDataBase.size()).isEqualTo(5);
    }

    @Test
    void itShouldReturnListPopularityArticles() {
        //Given
        List<Article> articles = getArticles();
        articleRepository.saveAll(articles);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        //When
        List<Article> articleFromDataBase = articleRepository.findArticleByPopularity(pageable);

        //Then
        assertThat(articleFromDataBase.get(0).getArticlePopularity())
                .isGreaterThan(articleFromDataBase.get(4).getArticlePopularity());
        assertThat(articleFromDataBase.size()).isEqualTo(5);
    }
    private List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();

        for (int i = 0; i < 8; i++){
            articles.add(Article.builder().journalist(journalistRepository.save(Journalist.builder()
                            .name("Journalist Test" + i)
                            .info("Test info")
                            .build()))
                    .title("title")
                    .articlePopularity((long) 1 + i)
                    .articleStatus(true)
                    .build());
        }
        return articles;
    }

    private Article getBuild() {
        return Article.builder()
                .journalist(journalistRepository.save(Journalist.builder()
                        .name("Journalist Test")
                        .info("Test info")
                        .build()))
                .title("Title")
                .articleStatus(true)
                .articlePopularity(1L)
                .build();
    }
}
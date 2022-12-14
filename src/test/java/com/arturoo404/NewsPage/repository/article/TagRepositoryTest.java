package com.arturoo404.NewsPage.repository.article;

import com.arturoo404.NewsPage.entity.news.article.Article;
import com.arturoo404.NewsPage.entity.news.journalist.Journalist;
import com.arturoo404.NewsPage.entity.news.tag.Tags;
import com.arturoo404.NewsPage.enums.Tag;
import com.arturoo404.NewsPage.repository.news.ArticleRepository;
import com.arturoo404.NewsPage.repository.news.JournalistRepository;
import com.arturoo404.NewsPage.repository.news.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JournalistRepository journalistRepository;

    @Test
    void itShouldFindAllByTagWhereArticleStatusIsFalse() {

        //Given
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        Article article = Article.builder()
                .title("Title")
                .articleStatus(false)
                .journalist(journalistRepository.save(
                        Journalist.builder()
                                .name("Journalist Test")
                                .info("Test info")
                                .build()))
                .build();

        article.setTags(List.of(new Tags(Tag.NEWS, article)));

        articleRepository.save(article);

        //When
        final Page<Tags> allByTag = tagRepository.findAllByTag(Tag.NEWS, pageable);

        //Then
        assertThat(allByTag.getTotalElements()).isZero();
    }

    @Test
    void itShouldFindAllByTagWhereArticleStatusIsTrue() {

        //Given
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        Article article = Article.builder()
                .title("Title")
                .articleStatus(true)
                .journalist(journalistRepository.save(
                        Journalist.builder()
                                .name("Journalist Test")
                                .info("Test info")
                                .build()))
                .build();

        article.setTags(List.of(new Tags(Tag.NEWS, article)));

        articleRepository.save(article);

        //When
        final Page<Tags> allByTag = tagRepository.findAllByTag(Tag.NEWS, pageable);

        //Then
        assertThat(allByTag.getTotalElements()).isGreaterThan(0);
        assertThat(allByTag.getTotalPages()).isNotZero();
    }
}
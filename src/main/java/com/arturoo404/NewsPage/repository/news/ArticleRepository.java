package com.arturoo404.NewsPage.repository.news;

import com.arturoo404.NewsPage.entity.news.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "FROM Article a WHERE a.id = ?1")
    Article findArticleById(Long id);

    @Query(value = "FROM Article a WHERE a.title LIKE %?1% AND a.articleStatus = true")
    Page<Article> findAllByKeyword(String keyword, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Article a SET a.articlePopularity = ?2 WHERE a.id = ?1")
    void updatePopularityRanking(Long id, long l);

    @Query(nativeQuery = true,
            value = "SELECT * FROM article WHERE article_status = true ORDER BY id desc LIMIT 5")
    List<Article> findArticleLastPublishedList();

    @Query(value = "FROM Article a WHERE a.articleStatus = true ORDER BY a.articlePopularity desc")
    List<Article> findArticleByPopularity(Pageable pageable);
}

package com.arturoo404.NewsPage.repository.news;

import com.arturoo404.NewsPage.entity.news.tag.Tags;
import com.arturoo404.NewsPage.enums.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tags, Long> {
    @Query(value = "FROM Tags t WHERE t.tag = ?1 AND t.articleTag.articleStatus = true")
    Page<Tags> findAllByTag(Tag tagConv, Pageable pageable);

    @Query(value = "FROM Tags t WHERE t.articleTag.articleStatus = true AND t.tag = ?1 ORDER BY t.articleTag.id desc")
    Page<Tags> findArticleLastPublishedListByTag(Pageable pageable, Tag tag);
}

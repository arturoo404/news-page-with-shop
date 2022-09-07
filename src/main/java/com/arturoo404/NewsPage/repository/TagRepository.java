package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.article.dto.TileArticleDto;
import com.arturoo404.NewsPage.entity.tag.Tags;
import com.arturoo404.NewsPage.enums.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tags, Long> {
    @Query(value = "FROM Tags t WHERE t.tag = ?1 AND t.articleTag.articleStatus = true")
    Page<Tags> findAllByTag(Tag tagConv, Pageable pageable);

    @Query(value = "FROM Tags t WHERE t.articleTag.articleStatus = true AND t.tag = ?1 ORDER BY t.articleTag.id desc")
    Page<Tags> findArticleLastPublishedListByTag(Pageable pageable, Tag tag);
}

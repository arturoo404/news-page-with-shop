package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.tag.Tags;
import com.arturoo404.NewsPage.enums.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tags, Long> {
    @Query(value = "FROM Tags t WHERE t.tag = ?1")
    Page<Tags> findAllByTag(Tag tagConv, Pageable pageable);
}

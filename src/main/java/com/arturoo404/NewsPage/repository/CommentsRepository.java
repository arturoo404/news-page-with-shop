package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.comments.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

   List<Comments> findAllCommentsByArticleIdOrderByIdDesc(Long articleId);
}

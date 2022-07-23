package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.journalist.Journalist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalistRepository extends JpaRepository<Journalist, Short> {
}

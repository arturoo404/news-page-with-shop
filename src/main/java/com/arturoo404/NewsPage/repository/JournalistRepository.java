package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.journalist.Journalist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalistRepository extends JpaRepository<Journalist, Short> {
    Optional<Journalist> findByName(String name);

    @Query(value = "FROM Journalist j WHERE j.id = ?1")
    Journalist findJournalistById(Short id);
}

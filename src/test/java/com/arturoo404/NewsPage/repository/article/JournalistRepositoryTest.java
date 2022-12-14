package com.arturoo404.NewsPage.repository.article;

import com.arturoo404.NewsPage.entity.news.journalist.Journalist;
import com.arturoo404.NewsPage.repository.news.JournalistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class JournalistRepositoryTest {

    @Autowired
    private JournalistRepository journalistRepository;

    private final String journalistName = "Journalist";

    @Test
    void itShouldFindByName() {
        //Given
        final Journalist test_info = journalistRepository.save(Journalist.builder()
                .name(journalistName)
                .info("Test info")
                .build());
        //When
        final Optional<Journalist> byName = journalistRepository.findByName(test_info.getName());

        //Then
        assertThat(byName).isPresent();
        assertThat(byName.get().getName()).isEqualTo(journalistName);
    }

    @Test
    void itShouldFindJournalistById() {
        //Given
        final Journalist test_info = journalistRepository.save(Journalist.builder()
                .name(journalistName)
                .info("Test info")
                .build());
        //When
        final Journalist journalistById = journalistRepository.findJournalistById(test_info.getId());

        //Then
        assertThat(journalistById.getName()).isNotNull();
        assertThat(journalistById.getId()).isNotZero();
    }
}
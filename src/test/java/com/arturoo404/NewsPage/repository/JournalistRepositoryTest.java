package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.journalist.Journalist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JournalistRepositoryTest {

    @Autowired
    private JournalistRepository journalistRepository;

    private final String journalistName = "Journalist";

    @BeforeEach
    void setUp() {
        journalistRepository.save(Journalist.builder()
                        .name(journalistName)
                        .info("Test info")
                .build());
    }

    @Test
    void itShouldFindByName() {
        //Given

        //When
        final Optional<Journalist> byName = journalistRepository.findByName(journalistName);

        //Then
        assertThat(byName).isPresent();
        assertThat(byName.get().getName()).isEqualTo(journalistName);
    }

    @Test
    void itShouldFindJournalistById() {
        //Given

        //When
        final Journalist journalistById = journalistRepository.findJournalistById((short) 1);

        //Then
        assertThat(journalistById.getName()).isNotNull();
        assertThat(journalistById.getId()).isNotZero();
    }
}
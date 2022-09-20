package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.entity.news.journalist.Journalist;
import com.arturoo404.NewsPage.entity.news.journalist.dto.JournalistAddDto;
import com.arturoo404.NewsPage.entity.news.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.news.JournalistRepository;
import com.arturoo404.NewsPage.service.impl.news.JournalistServiceImpl;
import com.arturoo404.NewsPage.service.news.JournalistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JournalistServiceImplTest {

    @Mock
    private JournalistRepository journalistRepository;

    private JournalistService journalistService;

    @BeforeEach
    void setUp() {
        journalistService = new JournalistServiceImpl(journalistRepository);
    }

    @Test
    void itShouldThrowExceptionWhenJournalistExistInDatabase() {
        //Given
        JournalistAddDto journalistAddDto = new JournalistAddDto("Journalist", "info");

        //When
        when(journalistRepository.findByName(anyString())).thenReturn(Optional.of(new Journalist()));
        try {
            journalistService.addJournalist(journalistAddDto);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("This journalist exist");
        }
    }

    @Test
    void itShouldAddJournalistToDatabaseAndReturnData() {
        //Given
        JournalistAddDto journalistAddDto = new JournalistAddDto("Journalist", "info");
        Journalist journalist = new Journalist();

        //When
        when(journalistRepository.save(any())).thenReturn(Journalist.builder()
                        .id((short) 1)
                        .name(journalistAddDto.getName())
                        .info(journalistAddDto.getInfo())
                .build());
        try {
            journalist = journalistService.addJournalist(journalistAddDto);
        } catch (ExistInDatabaseException ignored) {
        }
        //Then
        assertThat(journalist).isNotNull();
        assertThat(journalist.getName()).isEqualTo(journalistAddDto.getName());
    }

    @Test
    void itShouldReturnListOfJournalistInfo(){
        //Given
        given(journalistRepository.findAll()).willReturn(List.of(Journalist.builder()
                .id((short) 1)
                .name("Journalist")
                .info("info")
                .build()));
        //When
        final List<JournalistGetDto> journalistList = journalistService.getJournalistList();

        //Then
        assertThat(journalistList.size()).isNotZero();
        assertThat(journalistList.get(0).getName()).isNotBlank();
        assertThat(journalistList.get(0).getId()).isNotNull();
    }
}
package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.converter.TagConverter;
import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.ArticlePageDataDto;
import com.arturoo404.NewsPage.entity.article.dto.ArticleTitleDto;
import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import com.arturoo404.NewsPage.entity.article.dto.TileArticleDto;
import com.arturoo404.NewsPage.entity.content.Content;
import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.photo.ArticlePhoto;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.entity.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.entity.tag.Tags;
import com.arturoo404.NewsPage.enums.ContentType;
import com.arturoo404.NewsPage.enums.Tag;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.ArticlePhotoRepository;
import com.arturoo404.NewsPage.repository.ArticleRepository;
import com.arturoo404.NewsPage.repository.JournalistRepository;
import com.arturoo404.NewsPage.repository.TagRepository;
import com.arturoo404.NewsPage.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticlePhotoRepository articlePhotoRepository;

    @Mock
    private JournalistRepository journalistRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagConverter tagConverter;

    @BeforeEach
    void setUp() {
        articleService = new ArticleServiceImpl(
                articleRepository,
                articlePhotoRepository,
                journalistRepository,
                tagRepository,
                tagConverter
                );
    }

    @Test
    void itShouldAddArticleAndReturnIt() {
        //Given
        CreateArticleDto articleDto = CreateArticleDto.builder()
                .content("@text Test content")
                .journalist((short) 1)
                .title("Title")
                .tags(List.of("Tech", "News"))
                .build();

        //When
        when(journalistRepository.findJournalistById(anyShort())).thenReturn(Journalist.builder()
                .id((short) 1)
                .name("Journalist")
                .info("info")
                .build());

        when(articleRepository.save(any(Article.class))).thenReturn(Article.builder()
                .id(1L)
                .articleStatus(false)
                .tags(List.of(new Tags()))
                .title("title")
                .journalist(new Journalist())
                .content(List.of(new Content()))
                .build());

        Article article = articleService.addArticle(articleDto);

        //Then
        assertThat(article).isNotNull();
    }

    @Test
    void itShouldGetNumberOfPhotos() {
        //Given
        Article article = new Article();
        article.setId(1L);
        article.setContent(List.of(
                new Content(ContentType.PHOTO, new ArticlePhoto(), article),
                new Content(ContentType.TEXT, "Text", article)
        ));
        given(articleRepository.findArticleById(anyLong())).willReturn(article);

        //When
        final Integer numberOfPhotos = articleService.getNumberOfPhotos(1L);

        //Then
        assertThat(numberOfPhotos).isEqualTo(1);

    }

    @Test
    void itShouldSaveArticleStatistic() {
        //Given
        final ArticlePhotoAddDto addDto = ArticlePhotoAddDto.builder()
                .photoPlace("left")
                .photoHeight(500)
                .photoWidth(500)
                .articleId(1L)
                .photoPosition(1)
                .build();

        given(articlePhotoRepository.findByArticleIdAndPosition(anyLong(), anyInt())).willReturn(new ArticlePhoto()).getMock();

        //When
        when(articlePhotoRepository.save(any(ArticlePhoto.class))).thenReturn(new ArticlePhoto());
        final ArticlePhoto articlePhoto = articleService.saveArticleStatistic(addDto);

        //Then
        assertThat(articlePhoto).isNotNull();
    }

    @Test
    void itShouldThrowExceptionWhenArticleIsNotFound() {
        //Given
        given(articleRepository.findById(anyLong())).willReturn(Optional.empty());

        //When
        try {
            articleService.getMainArticlePhoto(1L);
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Photo not found.");
        }
        //Then
    }
    @Test
    void itShouldReturnMainPhoto() {
        //Given
        given(articleRepository.findById(anyLong())).willReturn(Optional.ofNullable(Article.builder()
                        .articleMainPhoto(new byte[]{10, 20, 30, 40, 50, 60, 70, 80})
                .build()));

        PhotoDto mainArticlePhoto = new PhotoDto();

        //When
        try {
            mainArticlePhoto = articleService.getMainArticlePhoto(1L);
        } catch (FileNotFoundException ignored) {
        }
        //Then
        assertThat(mainArticlePhoto.getPhoto().length).isNotZero();
    }

    @Test
    void getContent() throws ExistInDatabaseException {
        //Given
        Article article = getArticle();

        given(articleRepository.findArticleById(anyLong())).willReturn(article);

        //When
        final ArticlePageDataDto content = articleService.getContent(1L);

        //Then
        assertThat(content).isNotNull();
        assertThat(content.getJournalistGetDto().getName()).isNotEmpty();
        assertThat(content.getJournalistGetDto().getId()).isNotNull();
        assertThat(content.getTitle()).isNotNull();
        assertThat(content.getTags()).isEqualTo(List.of(Tag.NEWS));
        assertThat((int) content.getContentDto().stream()
                .filter(c -> c.getContentType().equals(ContentType.PHOTO)).count()).isNotZero();

    }

    @Test
    void itShouldReturnTitle() {
        //Given
        given(articleRepository.findArticleById(anyLong())).willReturn(Article.builder()
                .id(1L)
                .title("Title")
                .build());

        //When
        final ArticleTitleDto title = articleService.getTitle(1L);

        //Then
        assertThat(title.getTitle()).isNotBlank();

    }

    @Test
    void itShouldThrowExceptionWhenArticleInsidePhotoIsEmpty() {
        //Given
        given(articlePhotoRepository.findById(anyLong())).willReturn(Optional.empty());

        //When
        try {
            articleService.getArticleInsidePhoto(1L);
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Photo not found");
        }
    }

    @Test
    void itShouldReturnByteOfPhoto(){
        //Given
        given(articlePhotoRepository.findById(anyLong())).willReturn(Optional.of(ArticlePhoto.builder()
                .contentPhoto(new byte[]{10, 20, 20})
                .photoPosition(1)
                .photoWidth(500)
                .photoHeight(500)
                .photoPlace("left")
                .build()));

        PhotoDto articleInsidePhoto = new PhotoDto();

        //When
        try {
             articleInsidePhoto = articleService.getArticleInsidePhoto(1L);
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Photo not found");
        }

        //Then
        assertThat(articleInsidePhoto.getPhoto()).isNotEmpty();
    }

    @Test
    void itShouldReturnListOfPublishedArticle() {
        //Given
        given(articleRepository.findArticleLastPublishedList())
                .willReturn(List.of(getArticle()));

        //When
        final List<TileArticleDto> list = articleService.getLastPublishedArticleList();

        //Then
        assertThat(list.size()).isNotZero();
        assertThat(list.get(0).getTitle()).isNotNull();
    }

    @Test
    void itShouldReturnListOfLastPublishedArticleByTag() {
        //Given
        final Page<Tags> tags = new PageImpl<>(
                List.of(getArticle().getTags().toArray(new Tags[0]))
        );

        //When
        when(tagRepository.findArticleLastPublishedListByTag(any(), any()))
                .thenReturn(tags);

        final List<TileArticleDto> list = articleService.lastPublishedArticleByTagList("NEWS");

        //Then
        assertThat(list.size()).isNotZero();
        assertThat(list.get(0).getTitle()).isNotNull();
    }

    @Test
    void itShouldReturnArticleListByPopularity() {
        //Given
        final List<Article> articleList = List.of(getArticle());

        //When
        when(articleRepository.findArticleByPopularity(any()))
                .thenReturn(articleList);

        final List<TileArticleDto> list = articleService.getPopularityArticle();

        //Then
        assertThat(list.size()).isNotZero();
        assertThat(list.get(0).getTitle()).isNotNull();
    }

    private Article getArticle() {
        Article article = Article.builder()
                .id(1L)
                .title("Title")
                .articleStatus(true)
                .journalist(Journalist.builder()
                        .id((short) 1)
                        .name("Journalist")
                        .build())
                .build();
        article.setTags(List.of(new Tags(Tag.NEWS, article)));
        article.setContent(List.of(
                new Content(ContentType.TEXT, "Text", article),
                new Content(ContentType.PHOTO, ArticlePhoto.builder()
                        .photoPosition(1)
                        .photoWidth(500)
                        .photoHeight(500)
                        .photoPlace("left")
                        .build(), article)));
        return article;
    }
}
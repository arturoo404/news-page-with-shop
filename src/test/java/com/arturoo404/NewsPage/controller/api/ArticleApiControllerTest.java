package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.*;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.enums.Tag;
import com.arturoo404.NewsPage.service.ArticleService;
import com.arturoo404.NewsPage.service.CommentsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ArticleApiController.class)
class ArticleApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    private final String url = "/api/article";

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenContentIsBlank() throws Exception {
        //Given
        CreateArticleDto createArticleDto = CreateArticleDto.builder()
                .content("")
                .tags(List.of("news"))
                .title("Title")
                .journalist((short) 1)
                .build();

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(createArticleDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Content field can not be blank.");
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnCreatedWhenArticleWasSaved() throws Exception {
        //Given
        CreateArticleDto createArticleDto = CreateArticleDto.builder()
                .content("Content")
                .tags(List.of("news"))
                .title("Title")
                .journalist((short) 1)
                .build();

        //When
        doAnswer(invocationOnMock -> Article.builder()
                .id(1L)
                .title(invocationOnMock.getArgument(0, CreateArticleDto.class).getTitle())
                .build()).when(articleService).addArticle(any(CreateArticleDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(createArticleDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
    }

    @Test
    @WithMockUser
    void itShouldReturnBadRequestWhenMainPhotoIsEmpty() throws Exception {
        //Given
        MockMultipartFile photo = new MockMultipartFile("file", "filename.png", "image/jpeg", "".getBytes());

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url + "/photo/add/1").file(photo)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("You did not chose photo.");
    }

    @Test
    @WithMockUser
    void itShouldReturnOkStatusWhenMainPhotoWasSaved() throws Exception {
        //Given
        MockMultipartFile photo = new MockMultipartFile("file", "filename.png", "image/jpeg", "10, 10".getBytes());

        //When
        doAnswer(invocationOnMock -> {
            assertThat(invocationOnMock.getArguments()).isNotNull();
            return null;
        }).when(articleService).addArticlePhoto(any(MultipartFile.class), anyLong());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url + "/photo/add/1").file(photo)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Photo uploaded successfully.");
    }

    @Test
    @WithMockUser
    void itShouldDeleteArticleAndReturnStatusOk() throws Exception {
        //Given
        //When
        doAnswer(invocationOnMock -> {
            assertThat(invocationOnMock.getArgument(0, Long.class)).isNotNegative();
            return null;
        }).when(articleService).deleteArticle(anyLong());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url + "/delete/1")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Successfully delete article.");
    }

    @Test
    @WithMockUser
    void itShouldReturnNumberOfPhotoInsideArticleAndStatusOk() throws Exception {
        //Given
        given(articleService.getNumberOfPhotos(anyLong())).willReturn(3);

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/photo/content/1")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("3");
    }

    @Test
    @WithMockUser
    void savePhotoStatistic() throws Exception {
        //Given
        ArticlePhotoAddDto addDto = ArticlePhotoAddDto.builder()
                .photoPlace("left")
                .photoHeight(500)
                .photoWidth(500)
                .articleId(1L)
                .photoPosition(0)
                .build();
        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/photo/parameter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(addDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Select your photo position.");
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusOkWhenPhotoWasSaved() throws Exception {
        //Given
        MockMultipartFile photo = new MockMultipartFile("file", "filename.png", "image/jpeg", "10, 10".getBytes());

        //When
        doAnswer(invocationOnMock -> {
            assertThat(invocationOnMock.getArgument(0, MultipartFile.class).getBytes().length).isNotZero();
            assertThat(invocationOnMock.getArgument(1, Long.class)).isNotNegative();
            assertThat(invocationOnMock.getArgument(2, Integer.class)).isNotNegative();
            return null;
        }).when(articleService).savePhotoInsideArticle(any(MultipartFile.class), anyLong(), anyInt());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url + "/photo/inside").file(photo)
                        .param("articleId", "1")
                        .param("position", "1")
                .with(csrf())).andReturn();
        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Photo uploaded successfully.");
    }

    @Test
    @WithMockUser
    void itShouldReturnTileArticleDtoAndStatusOk() throws Exception {
        //Given
        Page<TileArticleDto> page = new PageImpl<>(List.of(new TileArticleDto(1L, "title")));
        //When
        when(articleService.getArticleTile(anyInt(), anyString())).thenReturn(page);

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/tile")
                        .param("page", "1")
                        .param("tag", "news")
                .with(csrf())).andReturn();
        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    @WithMockUser
    void itShouldReturnArticleContentAndStatusOk() throws Exception {
        //Given
        given(articleService.getContent(anyLong())).willReturn(new ArticlePageDataDto());

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/content")
                .param("articleId", "1")
                .with(csrf())).andReturn();
        //Then
        assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser
    void itShouldReturnArticleTitleAndStatusOk() throws Exception {
        //Given
        given(articleService.getTitle(anyLong())).willReturn(new ArticleTitleDto("title"));

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/title")
                .param("articleId", "1")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
    }

    @Test
    @WithMockUser
    void itShouldReturnArticleStatusPageAndStatusOk() throws Exception {
        //Given
        Page<ArticleStatusListDto> page = new PageImpl<>(List.of(new ArticleStatusListDto(1L, false, "title", "Jour")));

        //When
        when(articleService.getArticleList(anyInt())).thenReturn(page);

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/manage/list")
                .param("pageNumber", "1")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    @WithMockUser
    void itShouldChangeStatusAndReturnStatusOk() throws Exception {
        //Given
        //When
        doAnswer(invocationOnMock -> {
            assertThat(invocationOnMock.getArgument(0, Long.class)).isNotZero();
            return null;
        }).when(articleService).changeArticleStatus(anyLong());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/manage/status")
                        .param("articleId", "1")
                .with(csrf())).andReturn();
        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Status has changed.");
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
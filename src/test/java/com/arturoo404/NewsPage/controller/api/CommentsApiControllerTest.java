package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.service.CommentsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentsApiController.class)
class CommentsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentsService commentsService;

    private final String url = "/api/comments";

    @Test
    @WithMockUser
    void itShouldReturnBadRequestStatusWhenServiceThrowException() throws Exception {
        //Given
        AddCommentsDto addCommentsDto = addComments();

        //When
        doThrow(new Exception()).when(commentsService).addComments(any(AddCommentsDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(addCommentsDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnCreatedStatusWhenPostWasSaved() throws Exception {
        //Given
        AddCommentsDto addCommentsDto = addComments();

        //When
        doAnswer(invocationOnMock -> {
            assertThat(invocationOnMock.getArgument(0, AddCommentsDto.class).getContent()).isNotBlank();
            assertThat(invocationOnMock.getArgument(0, AddCommentsDto.class).getArticleId()).isNotNegative();
            assertThat(invocationOnMock.getArgument(0, AddCommentsDto.class).getEmail()).isNotBlank();
            return null;
        }).when(commentsService).addComments(any(AddCommentsDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(addCommentsDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
    }

    @Test
    @WithMockUser
    void itShouldReturnCommentsListAndStatusOk() throws Exception {
        //Given
        given(commentsService.getCommentsDetail(anyLong())).willReturn(List.of(getCommentsDetailList(),
                getCommentsDetailList(),
                getCommentsDetailList()));

        //When
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/list").param("articleId", "1")
                .with(csrf()))
                .andExpect(jsonPath("$[0].nick", is("nick")))
                .andExpect(jsonPath("$[0].content", is("Comments")))
                .andExpect(jsonPath("$[0].date", is("date")))
                .andExpect(status().isOk())
                .andReturn();

        //Then
    }

    private CommentsDetailDto getCommentsDetailList(){
        return new CommentsDetailDto(
                "nick",
                "Comments",
                "date"
        );
    }

    private AddCommentsDto addComments(){
        return new AddCommentsDto(
                "email@gmail.com",
                "test content",
                1L
        );
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
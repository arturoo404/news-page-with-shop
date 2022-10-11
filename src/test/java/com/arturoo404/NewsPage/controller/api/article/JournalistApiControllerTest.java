package com.arturoo404.NewsPage.controller.api.article;

import com.arturoo404.NewsPage.controller.api.news.JournalistApiController;
import com.arturoo404.NewsPage.entity.news.journalist.dto.JournalistAddDto;
import com.arturoo404.NewsPage.entity.news.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.news.JournalistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JournalistApiController.class)
class JournalistApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JournalistService journalistService;

    private final String url = "/api/journalist";

    @Test
    @WithMockUser
    void itShouldReturnStatusBadRequestWhenNameAndInfoIsEmpty() throws Exception {
        //Given
        JournalistAddDto journalistAddDto = new JournalistAddDto("", "");

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(journalistAddDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusCreatedWhenJournalistWasSaved() throws Exception {
        //Given
        JournalistAddDto journalistAddDto = new JournalistAddDto("Name", "Into");

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(journalistAddDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusBadRequestWhenJournalistExistInDatabase() throws Exception {
        //Given
        JournalistAddDto journalistAddDto = new JournalistAddDto("Name", "Into");

        //When
        doThrow(new ExistInDatabaseException("Journalist Exist in database."))
                .when(journalistService).addJournalist(any(JournalistAddDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(journalistAddDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Journalist Exist in database.");
    }

    @Test
    @WithMockUser
    void itShouldReturnStatusOkWhenJournalistPhotoWasSaved() throws Exception {
        //Given
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "10, 20".getBytes());

        //When
        doAnswer(invocationOnMock -> {
            assertThat(invocationOnMock.getArguments()).isNotNull();
            return null;
        }).when(journalistService).addJournalistPhoto(any(MultipartFile.class), anyShort());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url + "/photo/add/1").file(firstFile)
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Photo uploaded successfully.");
    }

    @Test
    @WithMockUser
    void itShouldReturnJournalistListWithStatusOk() throws Exception {
        //Given
        given(journalistService.getJournalistList()).willReturn(List.of(
                new JournalistGetDto((short) 1, "first"),
                new JournalistGetDto((short) 2, "second")
        ));

        //When
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/list")
                        .with(csrf()))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("first")))
                .andExpect(jsonPath("$[1].name", is("second")))
                .andExpect(status().isOk())
                .andReturn();
        //Then
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
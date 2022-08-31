package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.entity.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.service.UserService;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void itShouldReturnStatus400WhenValidFail() throws Exception {
        //Given
        UserRegistrationDto userDto = getBuild();

        //When
        when(userService.registerUser(any(UserRegistrationDto.class))).thenThrow(new ValidException("Test"));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnStatus200AndUserEntity() throws Exception {
        //Given
        UserRegistrationDto userDto = getBuild();

        //When
        doAnswer(invocationOnMock -> {
            assertThat(invocationOnMock.getArgument(0, UserRegistrationDto.class).getEmail()).isNotBlank();
            assertThat(invocationOnMock.getArgument(0, UserRegistrationDto.class).getNick()).isNotBlank();
            assertThat(invocationOnMock.getArgument(0, UserRegistrationDto.class).getPassword()).isNotBlank();
            return null;
        }).when(userService).registerUser(any(UserRegistrationDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
    }

    private UserRegistrationDto getBuild() {
        return UserRegistrationDto.builder()
                .nick("account")
                .password("password")
                .passwordConfirm("password")
                .email("email@gmail.com")
                .build();
    }


    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
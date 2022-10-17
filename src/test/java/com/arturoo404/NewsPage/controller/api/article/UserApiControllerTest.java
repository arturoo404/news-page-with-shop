package com.arturoo404.NewsPage.controller.api.article;

import com.arturoo404.NewsPage.controller.api.user.UserApiController;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangePasswordDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangeRoleDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.service.user.UserService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    @Test
    @WithMockUser
    void itShouldReturnStatusNoContentWhenOneOrMoreFieldWasEmpty() throws Exception {
        //Given
        UserChangePasswordDto userDto = new UserChangePasswordDto("pass", "pass", "", "acc");

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(204);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("One or more field is empty.");
    }

    @Test
    @WithMockUser
    void itShouldThrownValidExceptionWhenPasswordWasIncorrectAndReturnStatusBadRequest() throws Exception {
        //Given
        UserChangePasswordDto userDto = new UserChangePasswordDto("pass", "password", "pass", "acc");

        //When
        when(userService.changePassword(any(UserChangePasswordDto.class)))
                .thenThrow(new ValidException(any()));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }
    @Test
    @WithMockUser
    void itShouldThrownExistInDatabaseExceptionAndReturnStatusBadRequestWhenUserNotExist() throws Exception {
        //Given
        UserChangePasswordDto userDto = new UserChangePasswordDto("pass", "password", "pass", "acc");

        //When
        when(userService.changePassword(any(UserChangePasswordDto.class)))
                .thenThrow(new ExistInDatabaseException(any()));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnNoContentWhenEmailParmsWasBlankCurrentRole() throws Exception {
        //Given
        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/current-role")
                        .param("email", "")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(204);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Email field is empty.");
    }

    @Test
    @WithMockUser
    void itShouldThrowExistInDatabaseExceptionWhenUserNotExistCurrentRole() throws Exception {
        //Given
        //When
        when(userService.findCurrentRole(anyString()))
                .thenThrow(new ExistInDatabaseException(any()));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/current-role")
                .param("email", "email@gmail")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnRoleObjectAndStatusOkCurrentRole() throws Exception {
        //Given
        UserChangeRoleDto userChangeRoleDto = new UserChangeRoleDto("email@gmail.com", UserRole.ADMIN);

        //When
        when(userService.findCurrentRole(anyString())).thenReturn(userChangeRoleDto);

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/current-role")
                .param("email", "email@gmail")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotEmpty();
    }

    @Test
    @WithMockUser
    void itShouldReturnNoContentWhenEmailParmsWasBlankChangeRole() throws Exception {
        //Given
        UserChangeRoleDto userChangeRoleDto = new UserChangeRoleDto("", UserRole.ADMIN);

        //When
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/change-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userChangeRoleDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(204);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Email field is empty.");
    }

    @Test
    @WithMockUser
    void itShouldThrowExistInDatabaseExceptionWhenUserNotExistChangeRole() throws Exception {
        //Given
        UserChangeRoleDto userChangeRoleDto = new UserChangeRoleDto("email@gmail.com", UserRole.ADMIN);

        //When
        doThrow(new ExistInDatabaseException(""))
                .when(userService).changeUserRole(any(UserChangeRoleDto.class));

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/change-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userChangeRoleDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnRoleObjectAndStatusOkChangeRole() throws Exception {
        //Given
        UserChangeRoleDto userChangeRoleDto = new UserChangeRoleDto("email@gmail.com", UserRole.ADMIN);

        //When
        when(userService.findCurrentRole(anyString())).thenReturn(userChangeRoleDto);

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/change-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(userChangeRoleDto))
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Successfully changed user role.");
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
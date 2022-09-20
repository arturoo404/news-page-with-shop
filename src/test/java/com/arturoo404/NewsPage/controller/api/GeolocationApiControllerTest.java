package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.controller.api.news.GeolocationApiController;
import com.arturoo404.NewsPage.entity.news.city.CityDto;
import com.arturoo404.NewsPage.exception.NotFoundCityException;
import com.arturoo404.NewsPage.microservice.GeolocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GeolocationApiController.class)
class GeolocationApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeolocationService geolocationService;

    private final String url = "/api/geolocation";

    @Test
    @WithMockUser
    void itShouldThrowExceptionWhenCityWasNotFound() throws Exception {
        //Given
        //When
        doThrow(new NotFoundCityException(""))
                .when(geolocationService).getCity(anyString(), anyString());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/current")
                        .param("lat", "1")
                        .param("lon", "0")
                .with(csrf())).andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    @WithMockUser
    void itShouldReturnCityArrayAndStatusOK() throws Exception {
        //Given
        CityDto [] cityDto = {new CityDto("London", "GB")};
        //When
        when(geolocationService.getCity(anyString(), anyString())).thenReturn(cityDto);


        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/city")
                .param("lat", "1")
                .param("lon", "0")
                .with(csrf()))
                .andExpect(jsonPath("$[0].name", is("London")))
                .andExpect(jsonPath("$[0].country", is("GB")))
                .andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }
}
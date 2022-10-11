package com.arturoo404.NewsPage.controller.api.article;

import com.arturoo404.NewsPage.controller.api.news.WeatherApiController;
import com.arturoo404.NewsPage.entity.news.weather.dto.CurrentWeatherDto;
import com.arturoo404.NewsPage.entity.news.weather.dto.ForecastWeatherDto;
import com.arturoo404.NewsPage.entity.news.weather.model.CityStatistic;
import com.arturoo404.NewsPage.entity.news.weather.model.Temperature;
import com.arturoo404.NewsPage.entity.news.weather.model.WeatherDescription;
import com.arturoo404.NewsPage.entity.news.weather.model.WeatherList;
import com.arturoo404.NewsPage.exception.WeatherException;
import com.arturoo404.NewsPage.microservice.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(WeatherApiController.class)
class WeatherApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    private final String url = "/api/weather";

    @Test
    @WithMockUser
    void itShouldThrowExceptionWhenCityWasNotFound() throws Exception {
        //Given
        //When
        doThrow(new WeatherException(""))
                .when(weatherService).getCurrentWeatherInCity(anyString());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/current")
                .param("city", "notFound")
                .with(csrf())).andReturn();
        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    @WithMockUser
    void itShouldReturnWeatherInformationAndStatusOk() throws Exception {
        //Given
        CurrentWeatherDto currentWeatherDto = new CurrentWeatherDto("city",
                new Temperature("50"),
                List.of(new WeatherDescription("des")),
                "Rain");

        //When
        when(weatherService.getCurrentWeatherInCity(anyString()))
                .thenReturn(currentWeatherDto);

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/current")
                .param("city", "London")
                .with(csrf()))
                .andExpect(jsonPath("name", is("city")))
                .andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser
    void itShouldThrowExceptionWeatherExceptionAndReturnBadRequest() throws Exception {
        //Given
        //When
        doThrow(new WeatherException(""))
                .when(weatherService).getForecast(anyString());

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/forecast")
                .param("city", "notFound")
                .with(csrf())).andReturn();
        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(mvcResult.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    @WithMockUser
    void itShouldReturnWeatherForecastAndStatusOk() throws Exception {
        //Given
        ForecastWeatherDto forecastWeatherDto = new ForecastWeatherDto(
                List.of(new WeatherList(
                        new Temperature("100"),
                        "text",
                        List.of(new WeatherDescription(
                                "Des"
                        ))
                )),
                new CityStatistic(
                        "2000",
                        "London",
                        "GB"
                )
        );

        //When
        when(weatherService.getForecast(anyString()))
                .thenReturn(forecastWeatherDto);

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url + "/forecast")
                .param("city", "London")
                .with(csrf()))
                .andExpect(jsonPath("list[0].dt_txt", is("text")))
                .andExpect(jsonPath("list[0].main.temp", is("100")))
                .andExpect(jsonPath("city.population", is("2000")))
                .andExpect(jsonPath("city.name", is("London")))
                .andExpect(jsonPath("city.country", is("GB")))
                .andReturn();

        //Then
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }
}
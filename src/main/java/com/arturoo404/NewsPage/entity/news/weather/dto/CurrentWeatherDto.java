package com.arturoo404.NewsPage.entity.news.weather.dto;

import com.arturoo404.NewsPage.entity.news.weather.model.Temperature;
import com.arturoo404.NewsPage.entity.news.weather.model.WeatherDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeatherDto {

    private String name;
    private Temperature main;
    private List<WeatherDescription> weather;
    private String cod;
}

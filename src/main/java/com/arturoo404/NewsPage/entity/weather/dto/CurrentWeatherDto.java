package com.arturoo404.NewsPage.entity.weather.dto;

import com.arturoo404.NewsPage.entity.weather.model.Temperature;
import com.arturoo404.NewsPage.entity.weather.model.WeatherDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CurrentWeatherDto {

    private String name;
    private Temperature main;
    private List<WeatherDescription> weather;
    private String cod;
}

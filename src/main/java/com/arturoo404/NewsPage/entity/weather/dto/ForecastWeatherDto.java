package com.arturoo404.NewsPage.entity.weather.dto;

import com.arturoo404.NewsPage.entity.weather.model.CityStatistic;
import com.arturoo404.NewsPage.entity.weather.model.WeatherList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForecastWeatherDto {
    private List<WeatherList> list;
    private CityStatistic city;
}

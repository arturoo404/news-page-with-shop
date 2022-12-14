package com.arturoo404.NewsPage.entity.news.weather.dto;

import com.arturoo404.NewsPage.entity.news.weather.model.CityStatistic;
import com.arturoo404.NewsPage.entity.news.weather.model.WeatherList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForecastWeatherDto {
    private List<WeatherList> list;
    private CityStatistic city;
}

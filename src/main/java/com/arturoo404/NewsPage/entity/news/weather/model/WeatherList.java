package com.arturoo404.NewsPage.entity.news.weather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherList {
    private Temperature main;
    private String dt_txt;
    private List<WeatherDescription> weather;
}

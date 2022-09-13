package com.arturoo404.NewsPage.entity.weather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class WeatherList {
    private Temperature main;
    private String dt_txt;
    private List<WeatherDescription> weather;
}

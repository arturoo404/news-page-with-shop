package com.arturoo404.NewsPage.entity.weather.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class WeatherList {
    Temperature main;
    String dt_txt;
    private List<WeatherDescription> weather;
}

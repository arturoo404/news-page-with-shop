package com.arturoo404.NewsPage.microservice;

import com.arturoo404.NewsPage.exception.WeatherException;

public interface WeatherService {
    Object getCurrentWeatherInCity(String city) throws WeatherException;
}

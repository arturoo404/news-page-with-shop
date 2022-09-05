package com.arturoo404.NewsPage.microservice.impl;

import com.arturoo404.NewsPage.entity.weather.dto.CurrentWeatherDto;
import com.arturoo404.NewsPage.exception.WeatherException;
import com.arturoo404.NewsPage.microservice.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceImpl implements WeatherService{

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    private String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=";

    @Override
    public Object getCurrentWeatherInCity(String city) throws WeatherException {
        CurrentWeatherDto weather;
        try {
           weather  = restTemplate.getForObject(currentWeatherUrl + city +"&appid="+ apiKey + "&units=metric&cnt=1", CurrentWeatherDto.class);
        }catch (HttpClientErrorException e){
            throw new WeatherException("We can not found your city.");
        }
        return weather;
    }
}

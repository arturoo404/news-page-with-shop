package com.arturoo404.NewsPage.microservice.impl;

import com.arturoo404.NewsPage.entity.city.CityDto;
import com.arturoo404.NewsPage.entity.weather.dto.CurrentWeatherDto;
import com.arturoo404.NewsPage.exception.NotFoundCityException;
import com.arturoo404.NewsPage.exception.WeatherException;
import com.arturoo404.NewsPage.microservice.GeolocationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GeolocationServiceImpl implements GeolocationService {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    private String geolocationUrl = "https://api.openweathermap.org/geo/1.0/reverse?lat=";

    @Override
    public Object getCity(String lat, String lon) throws NotFoundCityException {
        CityDto[] city;
        try {
            city  = restTemplate.getForObject(geolocationUrl + lat +"&lon=" + lon + "&appid="+ apiKey + "&limit=1", CityDto[].class);
        }catch (HttpClientErrorException e){
            throw new NotFoundCityException("We can not found your city.");
        }
        return city;
    }
}

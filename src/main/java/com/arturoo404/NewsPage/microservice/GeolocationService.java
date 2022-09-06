package com.arturoo404.NewsPage.microservice;

import com.arturoo404.NewsPage.exception.NotFoundCityException;

public interface GeolocationService {
    Object getCity(String lat, String lon) throws NotFoundCityException;
}

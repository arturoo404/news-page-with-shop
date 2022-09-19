package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.exception.WeatherException;
import com.arturoo404.NewsPage.microservice.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/weather")
public class WeatherApiController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<Object> currentCityWeather(@RequestParam("city") String city){
        try {
            return ResponseEntity
                    .ok(weatherService.getCurrentWeatherInCity(city));
        } catch (WeatherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/forecast")
    public ResponseEntity<Object> forecastCityWeather(@RequestParam("city") String city){
        try {
            return ResponseEntity
                    .ok(weatherService.getForecast(city));
        } catch (WeatherException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}

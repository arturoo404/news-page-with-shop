package com.arturoo404.NewsPage.controller.templates.news;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/weather")
public class WeatherController {

    @GetMapping
    public String weather(){
        return "weather/weather";
    }
}

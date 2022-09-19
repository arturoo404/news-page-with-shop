package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.exception.NotFoundCityException;
import com.arturoo404.NewsPage.microservice.GeolocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/api/geolocation")
public class GeolocationApiController {

    private final GeolocationService geolocationService;

    public GeolocationApiController(GeolocationService geolocationService) {
        this.geolocationService = geolocationService;
    }

    @GetMapping("/city")
    public ResponseEntity<Object> getClientCity(@RequestParam("lat") String lat,
                                           @RequestParam("lon") String lon){
        try {
            return ResponseEntity
                    .ok(geolocationService.getCity(lat, lon));
        } catch (NotFoundCityException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}

package com.arturoo404.NewsPage.entity.weather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityStatistic {
    private String population;
    private String name;
    private String country;
}

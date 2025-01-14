package com.chiradev.weatherapp.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Builder
@Setter
@Getter
public class CityWeather {
    private String weather;
    private String details;
}

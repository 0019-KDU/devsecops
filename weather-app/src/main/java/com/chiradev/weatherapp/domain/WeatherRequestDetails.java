package com.chiradev.weatherapp.domain;

import com.chiradev.weatherapp.entity.WeatherResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class WeatherRequestDetails {

  private String cityName;
}

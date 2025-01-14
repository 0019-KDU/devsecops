package com.chiradev.weatherapp.transformer;

import com.chiradev.weatherapp.domain.CityWeather;
import com.chiradev.weatherapp.entity.OpenWeatherResponseEntity;
import com.chiradev.weatherapp.entity.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class OpenWeatherTransformer {

    public CityWeather transformToDomain(final OpenWeatherResponseEntity entity) {
        // Ensure the entity has a non-null 'weather' array
        if (entity.getWeather() == null || entity.getWeather().length == 0) {
            throw new IllegalArgumentException("Invalid OpenWeatherResponseEntity: 'weather' array is null or empty");
        }

        return CityWeather.builder()
                .weather(entity.getWeather()[0].getMain()) // Set the main weather (e.g., "Rain")
                .details(entity.getWeather()[0].getDescription()) // Set detailed description (e.g., "light rain")
                .build();
    }

    public WeatherResponse transformToEntity(final CityWeather cityWeather) {
        return WeatherResponse.builder()
                .weather(cityWeather.getWeather()) // Map the weather field
                .details(cityWeather.getDetails()) // Map the details field
                .build();
    }
}

package com.chiradev.weatherapp.service;

import com.chiradev.weatherapp.domain.CityCoordinates;
import com.chiradev.weatherapp.domain.CityWeather;
import com.chiradev.weatherapp.domain.WeatherRequestDetails;
import com.chiradev.weatherapp.entity.WeatherResponse;
import com.chiradev.weatherapp.provider.GeocodingProvider;
import com.chiradev.weatherapp.provider.WeatherProvider;
import com.chiradev.weatherapp.transformer.GeocodingCoordinatesTransformer;
import com.chiradev.weatherapp.transformer.OpenWeatherTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private  GeocodingProvider geocodingProvider;
    private  GeocodingCoordinatesTransformer geocodingCoordinatesTransformer;
    private  WeatherProvider weatherProvider;
    private OpenWeatherTransformer openWeatherTransformer;

    @Autowired
    public WeatherService(final GeocodingProvider geocodingProvider,
                          final GeocodingCoordinatesTransformer geocodingCoordinatesTransformer,
                          final WeatherProvider weatherProvider,
                          final OpenWeatherTransformer openWeatherTransformer) {
        this.geocodingProvider = geocodingProvider;
        this.geocodingCoordinatesTransformer = geocodingCoordinatesTransformer;
        this.weatherProvider = weatherProvider;
        this.openWeatherTransformer = openWeatherTransformer;
    }

    public WeatherResponse getWeather(final WeatherRequestDetails weatherRequestDetails) throws Exception {
        logger.info("Received weather request for: {}", weatherRequestDetails);

        try {
            logger.debug("Fetching coordinates for request: {}", weatherRequestDetails);
            final CityCoordinates cityCoordinates = geocodingCoordinatesTransformer.transformToDomain(
                    geocodingProvider.getCoordinates(weatherRequestDetails)
            );
            logger.info("Coordinates fetched successfully: {}", cityCoordinates);

            logger.debug("Fetching weather data for coordinates: {}", cityCoordinates);
            final CityWeather cityWeather = openWeatherTransformer.transformToDomain(
                    weatherProvider.getWeather(cityCoordinates)
            );
            logger.info("Weather data fetched successfully for coordinates: {}", cityCoordinates);

            logger.debug("Transforming weather data into WeatherResponse entity.");
            final WeatherResponse weatherResponse = openWeatherTransformer.transformToEntity(cityWeather);
            logger.info("Weather response created successfully: {}", weatherResponse);

            return weatherResponse;
        } catch (Exception e) {
            logger.error("Error occurred while processing weather request: {}", weatherRequestDetails, e);
            throw e;
        }
    }
}

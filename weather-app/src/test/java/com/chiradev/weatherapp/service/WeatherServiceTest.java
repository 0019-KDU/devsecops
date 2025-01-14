package com.chiradev.weatherapp.service;

import com.chiradev.weatherapp.domain.CityCoordinates;
import com.chiradev.weatherapp.domain.WeatherRequestDetails;
import com.chiradev.weatherapp.entity.GeocodingCoordinatesEntity;
import com.chiradev.weatherapp.entity.OpenWeatherResponseEntity;
import com.chiradev.weatherapp.entity.WeatherEntity;
import com.chiradev.weatherapp.entity.WeatherResponse;
import com.chiradev.weatherapp.provider.GeocodingProvider;
import com.chiradev.weatherapp.provider.WeatherProvider;
import com.chiradev.weatherapp.transformer.GeocodingCoordinatesTransformer;
import com.chiradev.weatherapp.transformer.OpenWeatherTransformer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
@WebMvcTest(WeatherService.class)
class WeatherServiceTest {

    private static final String CITY = "LONDON";
    private static final String LATITUDE = "11.28";
    private static final String LONGITUDE ="28.99" ;
    private static final String WEATHER ="Rain" ;
    public static final String DETAILS="A lot of rain";
    @MockitoBean
    private GeocodingProvider geocodingProvider;

    @MockitoBean
    private WeatherProvider weatherProvider;

    @MockitoBean
    private GeocodingCoordinatesTransformer geocodingCoordinatesTransformer;

    @MockitoBean
    private OpenWeatherTransformer openWeatherTransformer;

    @InjectMocks
    private WeatherService weatherService;


    @Test
    public void test_should_return_weather_response() throws Exception {
        final WeatherRequestDetails weatherRequestDetails= WeatherRequestDetails.builder()
                .cityName(CITY)
                .build();
        
        mockGeocodingProvider(weatherRequestDetails);
        mockGeocodingCoordinatesTransformer();
        mockWeatherProvider();
        mockOpenWeatherTransformer();

        final WeatherResponse weatherResponse = weatherService.getWeather(weatherRequestDetails);

        assertAll("Should return city weather response",
                ()->assertEquals(WEATHER,weatherResponse.getWeather()),
                ()->assertEquals(DETAILS,weatherResponse.getDetails()));
    }

    private void mockOpenWeatherTransformer() {
        final WeatherResponse weatherResponse=WeatherResponse.builder()
                .weather(WEATHER)
                .details(DETAILS)
                .build();
        when(openWeatherTransformer.transformToEntity(any())).thenReturn(weatherResponse);
    }

    private void mockWeatherProvider() throws Exception {
        final WeatherEntity weather=WeatherEntity.builder()
                .main(WEATHER)
                .description(DETAILS)
                .build();
        final WeatherEntity[] weatherEntities={weather};
        final OpenWeatherResponseEntity entity=OpenWeatherResponseEntity.builder()
                .weather(weatherEntities)
                .build();

        when(weatherProvider.getWeather(any())).thenReturn(entity);
    }

    private void mockGeocodingCoordinatesTransformer() {
        final CityCoordinates cityCoordinates=CityCoordinates.builder()
                .longitude(LONGITUDE)
                .latitude(LATITUDE)
                .build();

        when(geocodingCoordinatesTransformer.transformToDomain(any())).thenReturn(cityCoordinates);
    }

    private void mockGeocodingProvider(final WeatherRequestDetails weatherRequestDetails) throws Exception {
        final GeocodingCoordinatesEntity entity= GeocodingCoordinatesEntity.builder()
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .build();
        when(geocodingProvider.getCoordinates(weatherRequestDetails)).thenReturn(entity);
    }


}
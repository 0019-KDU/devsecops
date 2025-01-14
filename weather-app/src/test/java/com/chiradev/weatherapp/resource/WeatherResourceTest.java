package com.chiradev.weatherapp.resource;

import com.chiradev.weatherapp.domain.WeatherRequestDetails;
import com.chiradev.weatherapp.entity.WeatherResponse;
import com.chiradev.weatherapp.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(WeatherResource.class)
class WeatherResourceTest {

    public static final String CITY = "San Francisco";
    public static final String WEATHER = "Sunny";
    public static final String DETAILS = "Sunny";

    @MockitoBean
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_should_return_weather_response_success() throws Exception {
        final WeatherRequestDetails requestDetails=WeatherRequestDetails.builder()
                .cityName(CITY)
                .build();

        final WeatherResponse weatherResponse=WeatherResponse.builder()
                .weather(WEATHER)
                .details(DETAILS)
                .build();

        when(weatherService.getWeather(requestDetails)).thenReturn(weatherResponse);

        mockMvc.perform(get("/api/v1/weather/{city}",CITY)).andDo(print())
                .andExpect(status().isOk());
    }
}
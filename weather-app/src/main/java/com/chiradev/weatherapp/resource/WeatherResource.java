package com.chiradev.weatherapp.resource;

import com.chiradev.weatherapp.domain.WeatherRequestDetails;
import com.chiradev.weatherapp.entity.WeatherResponse;
import com.chiradev.weatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class WeatherResource {

    private  WeatherService weatherService;

    @Autowired
    public WeatherResource (final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/{city}")
    public @ResponseBody WeatherResponse weather(@PathVariable("city") String city) throws Exception {
        final WeatherRequestDetails weatherRequestDetails = WeatherRequestDetails.builder()
                .cityName(city)
                .build();

        // Debugging log
        System.out.println("WeatherRequestDetails: " + weatherRequestDetails);

        return weatherService.getWeather(weatherRequestDetails);
    }

}

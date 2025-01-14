package com.chiradev.weatherapp.transformer;

import com.chiradev.weatherapp.domain.CityCoordinates;
import com.chiradev.weatherapp.entity.GeocodingCoordinatesEntity;
import org.springframework.stereotype.Service;

@Service
public class GeocodingCoordinatesTransformer {

    public CityCoordinates transformToDomain(final GeocodingCoordinatesEntity entity){
        return CityCoordinates.builder()
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}

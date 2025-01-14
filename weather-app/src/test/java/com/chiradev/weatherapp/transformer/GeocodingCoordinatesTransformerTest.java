package com.chiradev.weatherapp.transformer;

import com.chiradev.weatherapp.domain.CityCoordinates;
import com.chiradev.weatherapp.entity.GeocodingCoordinatesEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeocodingCoordinatesTransformerTest {

    private GeocodingCoordinatesTransformer geocodingCoordinatesTransformer;

    @BeforeEach
    public void setUp() {
        geocodingCoordinatesTransformer = new GeocodingCoordinatesTransformer();
    }

    @Test
    public void test_should_transform_geocoding_coordinates_to_main(){
        final GeocodingCoordinatesEntity entity=
                GeocodingCoordinatesEntity.builder()
                        .longitude("12.0")
                        .latitude("30.0")
                        .build();

        final CityCoordinates cityCoordinates=geocodingCoordinatesTransformer.transformToDomain(entity);

        assertAll("Should return domain city coordinates",
                ()-> assertEquals(entity.getLatitude(),cityCoordinates.getLatitude()),
                ()-> assertEquals(entity.getLongitude(),cityCoordinates.getLongitude()));
    }
}
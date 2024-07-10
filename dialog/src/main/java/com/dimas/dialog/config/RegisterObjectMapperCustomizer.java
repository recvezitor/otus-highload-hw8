package com.dimas.dialog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import static com.dimas.util.ObjectMapperFactory.createObjectMapper;

@Singleton
@Slf4j
public class RegisterObjectMapperCustomizer implements ObjectMapperCustomizer {

    public void customize(ObjectMapper mapper) {
        createObjectMapper(mapper).
                setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

}
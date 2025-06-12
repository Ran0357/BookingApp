package com.example.demo.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // LocalDateTime → LocalTime
        mapper.addConverter(new AbstractConverter<LocalDateTime, LocalTime>() {
            @Override
            protected LocalTime convert(LocalDateTime source) {
                return source != null ? source.toLocalTime() : null;
            }
        });

        // LocalDateTime → LocalDate（必要なら）
        mapper.addConverter(new AbstractConverter<LocalDateTime, LocalDate>() {
            @Override
            protected LocalDate convert(LocalDateTime source) {
                return source != null ? source.toLocalDate() : null;
            }
        });

        return mapper;
    }
}

package com.jaynie.accombooking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

//@Service
//public class WeatherService {
//    private final RestTemplate restTemplate;
//    @Value("${weather.api.url}")
//    private String weatherApiUrl;
//
//    @Value("${weather.api.key}")
//    private String weatherApiKey;
//
//    public WeatherService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public Map<String, Object> getWeather(String city) {
//        String url = String.format("%s?q=%s&appid=%s", weatherApiUrl, city, weatherApiKey);
//
//        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody();
//        } else {
//            throw new RuntimeException("Failed to fetch weather data");
//        }
//    }
//}


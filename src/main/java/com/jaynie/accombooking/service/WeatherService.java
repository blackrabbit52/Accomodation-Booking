package com.jaynie.accombooking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {
    private static RestTemplate restTemplate = new RestTemplate();
    @Value("${https://api.openweathermap.org/data/2.5/forecast}")
    private static String weatherApiUrl;
    @Value("${2049cebde0b13d6ea8c639790e052b65}")
    private static String weatherApiKey;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static Map<String, Object> getWeather(String city) {
        String url = "api.openweathermap.org/data/2.5/forecast?q=London&appid=2049cebde0b13d6ea8c639790e052b65";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> weatherData = new HashMap<>();
            Map<String, Object> body = response.getBody();

            if (body != null) {
                Map<String, Object> main = (Map<String, Object>) body.get("main");
                System.out.println(main);

                if (main != null) {
                    weatherData.put("temperature", main.get("temp"));
                }

                List<Map<String, Object>> weatherList = (List<Map<String, Object>>) body.get("weather");
                if (weatherList != null && !weatherList.isEmpty()) {
                    weatherData.put("description", weatherList.get(0).get("description"));
                }
            }
            return weatherData;
        } else {
            throw new RuntimeException("Failed to fetch weather data");
        }
    }
}


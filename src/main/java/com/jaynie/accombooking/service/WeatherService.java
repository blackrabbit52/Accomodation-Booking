package com.jaynie.accombooking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WeatherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);
    private final RestTemplate restTemplate;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getWeather(String city) {
        Map<String, Object> weatherData = new HashMap<>();

        try {
            String url = String.format("%s?q=%s&appid=%s", weatherApiUrl, city, weatherApiKey);
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();

                if (body != null) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) body.get("list");

                    if (list != null && !list.isEmpty()) {
                        Map<String, Object> firstEntry = list.get(0);  // Take the first entry

                        // Extract "main" data
                        Map<String, Object> main = (Map<String, Object>) firstEntry.get("main");
                        if (main != null) {
                            weatherData.put("temperature", main.get("temp") + "°C");
                            weatherData.put("humidity", main.get("humidity") + "%");
                        }

                        // Extract "weather" array
                        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) firstEntry.get("weather");
                        if (weatherList != null && !weatherList.isEmpty()) {
                            weatherData.put("main", weatherList.get(0).get("main"));  // Example: "Snow"
                            weatherData.put("description", weatherList.get(0).get("description"));  // Example: "light snow"
                        }
                    }
                }
                // Log Retrieved Weather Data
                LOGGER.info("Weather data for {}: {}", city, weatherData);
            } else {
               LOGGER.warn("Failed to fetch weather data for {}: API returned {}", city, response.getStatusCode());
                weatherData.put("error", "Failed to fetch weather data");
            }
        } catch (Exception e) {
            LOGGER.error("Error fetching weather data for {}: {}", city, e.getMessage());
            weatherData.put("error", "Weather service unavailable. Please try again later.");
        }

        return weatherData;
    }
}
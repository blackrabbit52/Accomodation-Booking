package com.jaynie.accombooking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static com.mongodb.internal.authentication.AwsCredentialHelper.LOGGER;


@Service
public class GeocodeService {
   private final RestTemplate restTemplate;
    @Value("${weather.api.key}")
    private String weatherApiKey;

    public GeocodeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double[] getCoordinates(String city) {
        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", city, weatherApiKey);
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

        LOGGER.info("Geocode API response: " + response.getBody()); // Debugging

        if (response.getBody() != null && !response.getBody().isEmpty()) {
            Map<String, Object> location = (Map<String, Object>) response.getBody().get(0);
            double lat = Double.parseDouble(location.get("lat").toString());
            double lon = Double.parseDouble(location.get("lon").toString());

            LOGGER.info("Coordinates found: " + lat + ", " + lon);
            return new double[]{lat, lon};
        }

        LOGGER.warn("City not found: " + city);
        return null; // Return null if city not found
    }
}

package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.Room;
import com.jaynie.accombooking.service.DistanceService;
import com.jaynie.accombooking.service.GeocodeService;
import com.jaynie.accombooking.service.RoomService;
import com.jaynie.accombooking.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.*;


@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/room")

public class RoomController {
    @Autowired
    private final RoomService service;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private GeocodeService geocodeService;
    @Autowired
    private DistanceService distanceService;

    @Autowired
    public RoomController(RoomService service, GeocodeService geocodeService, DistanceService distanceService) {
        this.service = service;
        this.weatherService = weatherService;
        this.geocodeService = geocodeService;
        this.distanceService = distanceService;
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Room>> getRoomsByCity(@PathVariable("city") String city) {
        List<Room> byCity = service.getRoomsByCity(city);
        return ResponseEntity.ok(byCity);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") String id) {
        Optional<Room> byId = service.getRoomById(id);

        if (byId.isPresent()) {
            Room room = byId.get();
            String city = room.getLocation();

            Map<String, Object> weatherData = service.findWeatherByCity(city);

            System.out.println("Weather data in RoomController: " + weatherData); // Debugging

            if (weatherData != null && !weatherData.isEmpty()) {

                // Converted map to Weather object
                Room.Weather weather = new Room.Weather();
                weather.setTemperature((String) weatherData.get("temperature"));
                weather.setHumidity((String) weatherData.get("humidity"));
                weather.setDescription((String) weatherData.get("description"));
                weather.setMain((String) weatherData.get("main"));

                // Set the converted Weather object into Room
                // i used this to first test my weather data service
                room.setWeather(weather);
            }

            return new ResponseEntity<>(room, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> availableRooms = service.findAvailableRooms();
        return ResponseEntity.ok(availableRooms);
    }

    @GetMapping("/price")
    public ResponseEntity<List<Room>> getRoomsByPrice(@RequestParam int minPrice, @RequestParam int maxPrice) {
        return new ResponseEntity<>(service.findRoomsByPriceRange(minPrice, maxPrice), HttpStatus.OK);
    }

    @GetMapping("/furnished/{furnished}")
    public List<Room> getFurnishedRooms(@PathVariable boolean furnished) {
        return new ResponseEntity<>(service.getFurnishedRooms(furnished), HttpStatus.OK).getBody();
    }

    @GetMapping("/amenity/{amenity}")
    public List<Room> getRoomsByAmenity(@PathVariable String amenity) {
        return new ResponseEntity<>(service.getRoomsByAmenity(amenity), HttpStatus.OK).getBody();
    }

    @GetMapping("/postcode")
    public ResponseEntity<List<Room>> getRoomsByPostcode(@RequestParam("postcode") String postcode) {
        return new ResponseEntity<>(service.findRoomsByPostcode(postcode), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Room> addRoom(@RequestBody Room room) {
        try {
            // Save room to database
            Room savedRoom = service.saveRoom(room);

            // Fetch weather data based on room location
            String city = savedRoom.getLocation();
            Map<String, Object> weatherData = service.findWeatherByCity(city);

            if (weatherData != null && !weatherData.isEmpty()) {
                // Convert weather data to Room.Weather object
                Room.Weather weather = new Room.Weather();
                weather.setTemperature((String) weatherData.get("temperature"));
                weather.setHumidity((String) weatherData.get("humidity"));
                weather.setDescription((String) weatherData.get("description"));
                weather.setMain((String) weatherData.get("main"));

                // Attach weather data to room
                savedRoom.setWeather(weather);
            }
            return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        service.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> findAllRooms() {
        List<Room> rooms = service.findAllRooms();

        for (Room room : rooms) {
            String city = room.getLocation();
            Map<String, Object> weatherData = service.findWeatherByCity(city);

            System.out.println("Weather data for room in " + city + ": " + weatherData);

            if (weatherData != null && !weatherData.isEmpty()) {
                Room.Weather weather = new Room.Weather();
                weather.setTemperature((String) weatherData.get("temperature"));
                weather.setHumidity((String) weatherData.get("humidity"));
                weather.setDescription((String) weatherData.get("description"));
                weather.setMain((String) weatherData.get("main"));

                room.setWeather(weather);
            }
        }

        // Wrap rooms in a response object
        Map<String, Object> response = new HashMap<>();
        response.put("rooms", rooms);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/near")
    public ResponseEntity<List<Room>> getRoomsNearCity(@RequestParam String city, @RequestParam double maxDistance) {
        double[] userCoords = geocodeService.getCoordinates(city);
        if (userCoords == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Room> allRooms = service.findAllRooms();
        List<Room> nearbyRooms = allRooms.stream()
                .map(room -> {
                    double[] roomCoords = geocodeService.getCoordinates(room.getLocation());
                    if (roomCoords != null) {
                        double distance = distanceService.calculateDistance(userCoords[0], userCoords[1], roomCoords[0], roomCoords[1]);
                        if (distance <= maxDistance) {
                            // Fetch weather data
                            Map<String, Object> weatherData = weatherService.getWeather(room.getLocation());

                            // Convert Map to Weather object
                            Room.Weather weather = new Room.Weather();
                            weather.setTemperature((String) weatherData.get("temperature"));
                            weather.setHumidity((String) weatherData.get("humidity"));
                            weather.setDescription((String) weatherData.get("description"));
                            weather.setMain((String) weatherData.get("main"));

                            // Set weather and distance in Room
                            room.setWeather(weather);
                            room.setDistance(distance);
                            return room;
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(nearbyRooms);
    }
}

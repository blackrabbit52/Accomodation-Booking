package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.Room;
import com.jaynie.accombooking.service.RoomService;
//import com.jaynie.accombooking.service.WeatherService;
import com.jaynie.accombooking.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/room")

public class RoomController {
    @Autowired
    private final RoomService service;
    private WeatherService weatherService;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
        this.weatherService = weatherService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<>(service.findAllRooms(), HttpStatus.OK);
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

            // Fetch weather data from API
            Map<String, Object> weatherData = service.findWeatherByCity(city);

            System.out.println("Weather data in RoomController: " + weatherData); // Debugging

            if (weatherData != null && !weatherData.isEmpty()) {
                // Convert map to Weather object
                Room.Weather weather = new Room.Weather();
                weather.setTemperature((String) weatherData.get("temperature"));
                weather.setHumidity((String) weatherData.get("humidity"));
                weather.setDescription((String) weatherData.get("description"));
                weather.setMain((String) weatherData.get("main"));

                // Set the converted Weather object into Room
                room.setWeather(weather);
            }

            return new ResponseEntity<>(room, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms( ) {
        List<Room> availableRooms = service.findAvailableRooms( );
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
        return new ResponseEntity<>(service.findRoomsByPostcode(postcode),HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Room> addRoom(@RequestBody Room room) {
        Room savedRoom = service.saveRoom(room); // Save the room using the service
        return ResponseEntity.ok(savedRoom); // Return the saved room with HTTP 200 (OK)
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        service.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/weather/{city}")
//    public ResponseEntity<List<Room>> getRoomsWithWeather(@PathVariable String city) {
//        List<Room> rooms = service.findWeatherByCity(weather);
//        return ResponseEntity.ok(rooms);
//    }



//    @PutMapping("/{id}")
//    public ResponseEntity<Room> updateRoom(@PathVariable String id, @RequestBody Room updatedRoom) {
//        Room room = service.updateRoom(id, updatedRoom);
//        return ResponseEntity.ok(room);
//    }
}

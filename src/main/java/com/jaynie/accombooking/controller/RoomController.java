package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.Room;
import com.jaynie.accombooking.service.RoomService;
//import com.jaynie.accombooking.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room")

public class RoomController {
    @Autowired
    private final RoomService service;
//    private  WeatherService weatherService;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
//        this.weatherService = weatherService;

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
    public ResponseEntity<Optional<Room>> getRoomById(@PathVariable("id") String id) {
        Optional<Room> byId = service.getRoomById(id);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(@RequestParam boolean isAvailable) {
        List<Room> availableRooms = service.findAvailableRooms(isAvailable);
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

//    @PutMapping("/{id}")
//    public ResponseEntity<Room> updateRoom(@PathVariable String id, @RequestBody Room updatedRoom) {
//        Room room = service.updateRoom(id, updatedRoom);
//        return ResponseEntity.ok(room);
//    }
}

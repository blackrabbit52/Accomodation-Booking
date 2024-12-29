package com.jaynie.accombooking.controller;

import com.jaynie.accombooking.model.Room;
import com.jaynie.accombooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/room")

public class RoomController {
    @Autowired
    private final RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<>(service.findAllRooms(), HttpStatus.OK);
    }

    @GetMapping("/rooms/{city}")
    public ResponseEntity<List<Room>> getRoomsByCity(@PathVariable String city) {
        return new ResponseEntity<>(service.getRoomsByCity(city), HttpStatus.OK);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        return service.getRoomById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(@RequestParam boolean isAvailable) {
        List<Room> availableRooms = service.findAvailableRooms(isAvailable);
        return ResponseEntity.ok(availableRooms);
    }

    @GetMapping("/by-price")
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
    @GetMapping("/by-postcode")
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

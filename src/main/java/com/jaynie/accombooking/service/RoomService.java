package com.jaynie.accombooking.service;

import com.jaynie.accombooking.model.Room;
import com.jaynie.accombooking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }
    public List<Room> getRoomsByCity(String city) {
        return roomRepository.findByCity(city);
    }
    public List<Room> findRoomsByFurnished(boolean furnished) {
        return roomRepository.findByFurnished(furnished);
    }
    public List<Room> findRoomsByAmenity(String amenity) {
        return roomRepository.findByAmenitiesContaining(amenity);
    }
    public List<Room> findRoomsByPriceRange(int minPrice, int maxPrice) {
        return roomRepository.findByPricePerMonthGbpBetween(minPrice, maxPrice);
    }
    public List<Room> findAvailableRooms(boolean isAvailable) {
        return roomRepository.findByIsAvailable(isAvailable);
    }

    // Find rooms by postcode
    public List<Room> findRoomsByPostcode(String postcode) {
        return roomRepository.findByPostcode(postcode);
    }

    // Get room by ID
    public Optional<Room> getRoomById(String id) {
        return roomRepository.findById(id);
    }

    // Add a new room
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    // Delete a room by ID
    public void deleteRoomById(String id) {
        if (roomRepository.existsById(id)) {roomRepository.deleteById(id);} else {
            throw new RuntimeException("Room not found with ID: " + id);
        }
    }

    // Update an existing room
//    public Room updateRoom(String id, Room updatedRoom) {
//        return roomRepository.findById(id).map(room -> {
//            room.setCity(updatedRoom.getLocation());
//            room.setPricePerMonth(updatedRoom.getPricePerMonth());
//            room.setSpokenLanguages(updatedRoom.getSpokenLanguages());
//            room.setDescription(updatedRoom.getDescription());
//            room.setRoomType(updatedRoom.getRoomType());
//            room.setAvailable(updatedRoom.isAvailable());
//            return roomRepository.save(room);
//        }).orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
//    }


}

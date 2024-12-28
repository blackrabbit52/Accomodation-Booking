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
        return roomRepository.findAll();}

    public List<Room> getRoomsByCity(String city) {
        return roomRepository.findByLocation_City(city);}

    public List<Room> getFurnishedRooms(boolean furnished) {
        return roomRepository.findByDetailsFurnished(furnished);
    }

    public List<Room> getRoomsByAmenity(String amenity) {
        return roomRepository.findByDetailsAmenitiesContaining(amenity);
    }
    public List<Room> findRoomsByPriceRange(int minPrice, int maxPrice) {
        return roomRepository.findByPricePerMonthGbpBetween(minPrice, maxPrice);}

    public List<Room> findAvailableRooms(boolean isAvailable) {
        return roomRepository.findByIsAvailable(isAvailable);}

    public List<Room> findRoomsByPostcode(String postcode) {
        return roomRepository.findByLocation_Postcode(postcode);}

    public Optional<Room> getRoomById(String id) {
        return roomRepository.findById(id);}

    public Room saveRoom(Room room) {
        return roomRepository.save(room);}

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

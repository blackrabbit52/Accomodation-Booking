package com.jaynie.accombooking.repositories;

import com.jaynie.accombooking.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    @Override
    List<Room> findAll();
    Optional<Room> findById(String id);
    List<Room> findByLocation_City(String city);
    List<Room> findByDetailsFurnished(boolean furnished);
    List<Room> findByDetailsAmenitiesContaining(String amenity);
    List<Room> findByPricePerMonthGbpBetween(int minPrice, int maxPrice);
    List<Room> findByIsAvailable(boolean isAvailable);
    List<Room> findByLocation_Postcode(String postcode);
}

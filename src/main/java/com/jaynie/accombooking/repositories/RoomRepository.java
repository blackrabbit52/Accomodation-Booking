package com.jaynie.accombooking.repositories;

import com.jaynie.accombooking.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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

    @Query("{'price_per_month_gbp': {$gte: ?0, $lte: ?1}}")
    List<Room> findByPrice_per_month_gbpBetween(int minPrice, int maxPrice);

    @Query("{ 'available' : true }")
    List<Room> findByAvailableTrue();
    List<Room> findByLocation_Postcode(String postcode);

}

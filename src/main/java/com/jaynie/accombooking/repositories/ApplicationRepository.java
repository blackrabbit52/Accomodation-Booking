package com.jaynie.accombooking.repositories;

import com.jaynie.accombooking.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findAll();
    List<Application> findByUserId(String userId);
    List<Application> findByName(String name);
    List<Application> findByRoomId(String roomId);
    List<Application> findByApplicationId(String applicationId);
    List<Application> findByStatus(String status);
}
package com.jaynie.accombooking.repositories;

import com.jaynie.accombooking.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findAll();
    List<Application> findByUserId(String userId);
    List<Application> findByRoomId(String roomId);

}
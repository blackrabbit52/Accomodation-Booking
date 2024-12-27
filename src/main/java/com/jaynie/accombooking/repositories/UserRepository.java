package com.jaynie.accombooking.repositories;
import com.jaynie.accombooking.model.Room;
import com.jaynie.accombooking.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    List<User> findByName(String name);



}

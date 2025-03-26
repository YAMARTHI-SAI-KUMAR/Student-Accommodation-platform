package com.Accommodation.StudentAccommodationPlatform.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Accommodation.StudentAccommodationPlatform.Entities.User;

public interface UserRepo extends MongoRepository<User, String>{

    Optional<User> findByEmail(String email);
    
}

package com.Accommodation.StudentAccommodationPlatform.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Accommodation.StudentAccommodationPlatform.Entities.Image;

public interface ImageRepo extends MongoRepository<Image, String> {
    Optional<Image> findByName(String fileName);
    
}

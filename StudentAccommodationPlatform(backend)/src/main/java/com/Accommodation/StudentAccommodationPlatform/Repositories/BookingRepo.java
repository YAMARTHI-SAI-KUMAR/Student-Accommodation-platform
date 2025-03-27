package com.Accommodation.StudentAccommodationPlatform.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Accommodation.StudentAccommodationPlatform.Entities.Booking;

public interface BookingRepo extends MongoRepository<Booking,String> {

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    List<Booking> findByCheckInDateBetweenOrCheckOutDateBetween(LocalDate checkInDate, LocalDate checkOutDate);

    
}

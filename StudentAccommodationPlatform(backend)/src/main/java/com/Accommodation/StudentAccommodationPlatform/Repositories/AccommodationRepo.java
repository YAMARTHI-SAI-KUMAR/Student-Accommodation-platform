// package com.Accommodation.StudentAccommodationPlatform.Repositories;

// import com.Accommodation.StudentAccommodationPlatform.Entities.Accommodation;
// import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;
// import org.springframework.data.repository.query.Param;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;

// public interface AccommodationRepo extends MongoRepository<Accommodation, String> {

//     void deleteById(String roomId);  // MongoDB ID is a String, not Long

//     Optional<Accommodation> findById(String roomId);

//     // Finding available rooms by checking if no booking overlaps the given dates
//     @Query("{ 'roomType': { $regex: ?2, $options: 'i' }, " + 
//            "'_id': { $nin: [ { $expr: { $gt: [ '$bookings.checkInDate', ?1 ] } }, " + 
//                           "{ $expr: { $lt: [ '$bookings.checkOutDate', ?0 ] } } ] } }")
//     List<Accommodation> findAvailableRoomsByDatesAndTypes(
//             @Param("checkInDate") LocalDate checkInDate,
//             @Param("checkOutDate") LocalDate checkOutDate,
//             @Param("roomType") String roomType
//     );

//     // Fetch all available rooms (rooms without bookings)
//     @Query("{ 'bookings': { $exists: false } }")
//     List<Accommodation> getAllAvailableRooms();

//     // Find distinct room types
//     @Query(value = "{}", fields = "{'roomType': 1, '_id': 0}")
//     List<String> findDistinctRoomTypes();
// }
package com.Accommodation.StudentAccommodationPlatform.Repositories;

import com.Accommodation.StudentAccommodationPlatform.Entities.Accommodation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccommodationRepo extends MongoRepository<Accommodation, String> {

    void deleteById(String roomId);  // MongoDB ID is a String, not Long

    Optional<Accommodation> findById(String roomId);

    // Finding available rooms by checking if no booking overlaps the given dates
    @Query("{ 'roomType': { $regex: ?2, $options: 'i' }, " + 
           "'_id': { $nin: [ { $expr: { $gt: [ '$bookings.checkInDate', ?1 ] } }, " + 
                          "{ $expr: { $lt: [ '$bookings.checkOutDate', ?0 ] } } ] } }")
    List<Accommodation> findAvailableRoomsByDatesAndTypes(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("roomType") String roomType
    );

    // Fetch all available rooms (rooms without bookings)
    @Query("{ 'bookings': { $exists: false } }")
    List<Accommodation> getAllAvailableRooms();

    // Find distinct room types
    @Query(value = "{}", fields = "{'roomType': 1, '_id': 0}")
    List<String> findDistinctRoomTypes();

    List<Accommodation> findByRoomTypeLikeAndIdNotIn(String roomType, List<String> bookedRoomsId);


       
}

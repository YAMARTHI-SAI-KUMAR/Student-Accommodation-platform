// package com.Accommodation.StudentAccommodationPlatform.DTO;

// import java.util.List;

// import com.fasterxml.jackson.annotation.JsonInclude;

// import lombok.Data;

// @Data
// @JsonInclude(JsonInclude.Include.NON_NULL)
// public class Response {

//        private int statusCode;
//     private String message;

//     private String token;
//     private String role;
//     private String expirationTime;
//     private String bookingConfirmationCode;

//     private UserDto user;
//     private AccommodationDto room;
//     private BookingDto booking;
//     private List<UserDto> userList;
//     private List<AccommodationDto> roomList;
//     private List<BookingDto> bookingList;
    
// }

package com.Accommodation.StudentAccommodationPlatform.DTO;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON response
public class Response<T> { // Generic class for flexibility

    private int statusCode;
    private String message;

    // Authentication-related fields
    private String token;
    private String role;
    private String expirationTime;

    // Booking confirmation
    private String bookingConfirmationCode;

    // Generic data field to hold any response data (UserDto, AccommodationDto, BookingDto, etc.)
    private T data;

    // List for handling multiple objects (Users, Accommodations, Bookings)
    private List<T> dataList;
}


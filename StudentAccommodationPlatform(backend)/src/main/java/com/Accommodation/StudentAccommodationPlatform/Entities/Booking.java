// package com.Accommodation.StudentAccommodationPlatform.Entities;

// import org.springframework.data.mongodb.core.mapping.DBRef;
// import org.springframework.data.mongodb.core.mapping.Document;

// import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.NotBlank;

// import java.time.LocalDate;

// import org.springframework.data.annotation.Id;

// import lombok.*;

// @Data
// @Document(collection="booking")
// public class Booking {


//     @Id
//     private String id;

//     private Accommodation accommodation;


   
//     @NotBlank(message = "check in date is required")
//     private LocalDate checkInDate;

//     @NotBlank(message = "check out date is required")
//     private LocalDate checkOutDate;
    


//    @Min(value= 1, message="Number of customer should not be less than one")
//     private  int noOfCustomers;

//     private String status;

//     private String bookingConfirmationCode;

//     @DBRef
//     private User user;


// }


package com.Accommodation.StudentAccommodationPlatform.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    @DBRef
    private Accommodation accommodation;

    @NotNull(message = "Check-in date is required")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Number of customers should not be less than one")
    private int noOfCustomers;

    private String status;
    private BookingType bookingType; // "temporary" or "permanent"
    private String bookingConfirmationCode;

    @DBRef
    private User user;

    public boolean isActive() {
        return checkOutDate.isAfter(LocalDate.now());
    }
}

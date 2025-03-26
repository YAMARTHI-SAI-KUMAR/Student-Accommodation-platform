package com.Accommodation.StudentAccommodationPlatform.DTO;

import java.util.*;

import com.Accommodation.StudentAccommodationPlatform.Entities.Booking;
import com.fasterxml.jackson.annotation.JsonInclude;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private String id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingDTO>bookings=new ArrayList<>();

    
}

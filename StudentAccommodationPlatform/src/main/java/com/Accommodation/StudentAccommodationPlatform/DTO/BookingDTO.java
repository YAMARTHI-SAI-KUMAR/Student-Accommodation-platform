package com.Accommodation.StudentAccommodationPlatform.DTO;

import java.time.LocalDate;



import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {
    private String id;
    private AccommodationDTO accommodation;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int noOfCustomers;
    private String status;
    private String bookingConfirmationCode;
    private UserDTO user;



}

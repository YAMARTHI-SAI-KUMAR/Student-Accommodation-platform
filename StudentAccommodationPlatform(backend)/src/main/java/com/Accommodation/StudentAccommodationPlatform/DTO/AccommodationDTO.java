package com.Accommodation.StudentAccommodationPlatform.DTO;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccommodationDTO {
    private String id;
    private String bhkType;
    private String address;
    private String accommodationType;
    private BigDecimal price;
    private String accommodationPhotoUrl;
    private String accommodationDescription;
    private boolean isAvailable; 
    private List<BookingDTO>bookings;



}

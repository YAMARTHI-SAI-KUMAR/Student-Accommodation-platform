package com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess;

import com.Accommodation.StudentAccommodationPlatform.DTO.BookingDTO;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.Entities.Booking;

public interface BookingService {
    Response<BookingDTO> saveBooking(String accoId, String userId, Booking bookingRequest);

    Response<BookingDTO> findBookingByConfirmationCode(String confirmationCode);

    Response<BookingDTO> getAllBookings();

    Response<BookingDTO> cancelBooking(String bookingId);
}

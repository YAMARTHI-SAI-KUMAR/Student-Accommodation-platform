package com.Accommodation.StudentAccommodationPlatform.Controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Accommodation.StudentAccommodationPlatform.DTO.BookingDTO;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.Entities.Booking;
import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.BookingService;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings")

public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;
    
     @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response<BookingDTO>> saveBooking(
            @PathVariable String roomId,
            @PathVariable String userId,
            @Valid @RequestBody Booking bookingRequest) {

        logger.info("Received booking request for room {} by user {}", roomId, userId);

        Response<BookingDTO> response = bookingService.saveBooking(roomId, userId, bookingRequest);

        if (response.getStatusCode() == 400) {
            return ResponseEntity.badRequest().body(response);
        } else if (response.getStatusCode() == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (response.getStatusCode() == 500) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<BookingDTO>> getAllBookings() {
        logger.info("Fetching all bookings...");
        
        Response<BookingDTO> response = bookingService.getAllBookings();
        
        if (response.getStatusCode() != 200) {
            logger.error("Error fetching bookings: {}", response.getMessage());
        } else {
            logger.info("Successfully fetched {} bookings", 
                         response.getDataList() != null ? response.getDataList().size() : 0);
        }
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response<BookingDTO>> getBookingsByConfirmationCode(@PathVariable String confirmationCode) {
        logger.info("Fetching booking with confirmation code: {}", confirmationCode);
    
        if (confirmationCode == null || confirmationCode.trim().isEmpty()) {
            logger.warn("Invalid confirmation code received.");
            Response<BookingDTO> errorResponse = new Response<>();
            errorResponse.setStatusCode(400);
            errorResponse.setMessage("Invalid confirmation code.");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    
        Response<BookingDTO> response = bookingService.findBookingByConfirmationCode(confirmationCode);
    
        if (response.getStatusCode() != 200) {
            logger.error("Booking not found or error occurred: {}", response.getMessage());
        } else {
            logger.info("Successfully fetched booking with confirmation code: {}", confirmationCode);
        }
    
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/cancel/{bookingId}")
@PreAuthorize("hasAuthority('ADMIN')")
public ResponseEntity<Response<BookingDTO>> cancelBooking(@PathVariable String bookingId) {
    logger.info("Received request to cancel booking with ID: {}", bookingId);

    if (bookingId == null || bookingId.trim().isEmpty()) {
        logger.warn("Invalid booking ID received for cancellation.");
        Response<BookingDTO> errorResponse = new Response<>();
        errorResponse.setStatusCode(400);
        errorResponse.setMessage("Invalid booking ID.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    Response<BookingDTO> response = bookingService.cancelBooking(bookingId);

    if (response.getStatusCode() == 404) {
        logger.warn("Booking not found for ID: {}", bookingId);
    } else if (response.getStatusCode() == 500) {
        logger.error("Error occurred while cancelling booking ID: {}", bookingId);
    } else {
        logger.info("Successfully cancelled booking with ID: {}", bookingId);
    }

    return ResponseEntity.status(response.getStatusCode()).body(response);
}

    
    
}

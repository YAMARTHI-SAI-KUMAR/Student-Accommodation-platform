package com.Accommodation.StudentAccommodationPlatform.Service.Impl;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Accommodation.StudentAccommodationPlatform.DTO.BookingDTO;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.Entities.Accommodation;
import com.Accommodation.StudentAccommodationPlatform.Entities.Booking;
import com.Accommodation.StudentAccommodationPlatform.Entities.BookingType;
import com.Accommodation.StudentAccommodationPlatform.Entities.User;
import com.Accommodation.StudentAccommodationPlatform.Exception.OurException;
import com.Accommodation.StudentAccommodationPlatform.Repositories.AccommodationRepo;
import com.Accommodation.StudentAccommodationPlatform.Repositories.BookingRepo;
import com.Accommodation.StudentAccommodationPlatform.Repositories.UserRepo;
import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.BookingService;
import com.Accommodation.StudentAccommodationPlatform.utils.Utils;

@Service
public class BookingServiceImpl implements BookingService {


    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private AccommodationRepo accommodationRepo;
    @Autowired
    private UserRepo userRepo;


    
    

    // private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private static final String TEMPORARY = "temporary";
    private static final String PERMANENT = "permanent";

    // @Autowired
    // private BookingType bookingType;

    @Override
    public Response<BookingDTO> saveBooking(String accoId, String userId, Booking bookingRequest) {
        Response<BookingDTO> response = new Response<>();
        // logger.info("Accommodation Type: {}", accommodation.getAccommodationType());

    
        try {
            Accommodation accommodation = accommodationRepo.findById(accoId)
                    .orElseThrow(() -> new OurException("Room not found"));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
    
            List<Booking> existingBookings = accommodation.getBookings();
    
            if (accommodation.getAccommodationType() != null && 
    accommodation.getAccommodationType().toString().equalsIgnoreCase(BookingType.PERMANENT.name())) {
    // Proceed with the logic


                {
                // Permanent Booking: Check if accommodation is already occupied
                if (!existingBookings.isEmpty()) {
                    throw new OurException("Accommodation is already booked for a long-term lease.");
                }
            }
        }
        
        else if (accommodation.getAccommodationType() != null && 
        accommodation.getAccommodationType().toString().equalsIgnoreCase(BookingType.TEMPORARY.name())){

                 // Temporary Booking Validation
            if (bookingRequest.getCheckInDate() == null || bookingRequest.getCheckOutDate() == null) {
                throw new OurException("Both check-in and check-out dates are required for temporary booking.");
            }
                // Temporary Booking: Validate date range
                if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                    throw new IllegalArgumentException("Check-in date must be before check-out date.");
                }

                long daysBetween = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
              if (daysBetween > 90) {
                throw new OurException("Temporary stay cannot exceed 90 days.");
                }
                if (!roomIsAvailable(bookingRequest, existingBookings)) {
                    throw new OurException("Room not available for the selected date range.");
                }
            } 
            
            // else {
            //     throw new OurException("Invalid booking type. Choose 'temporary' or 'permanent'.");
            // }
            
    
            // Generate confirmation code
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setAccommodation(accommodation);
            bookingRequest.setUser(user);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
    
            // Save booking
            Booking savedBooking = bookingRepo.save(bookingRequest);
    
            // Update user's booking list
            List<Booking> userBookings = user.getBookings();
            userBookings.add(savedBooking);
            user.setBookings(userBookings);
            userRepo.save(user);
    
            response.setStatusCode(200);
            response.setMessage("Booking successful.");
            response.setBookingConfirmationCode(bookingConfirmationCode);
    
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving booking: " + e.getMessage());
        }
    
        return response;
    }
    
    @Override
    public Response<BookingDTO> getAllBookings() {
        Response<BookingDTO> response = new Response<>();

        try {
            List<Booking> bookingList = bookingRepo.findAll(Sort.by(Sort.Direction.DESC, "_id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setMessage("successful");
            response.setStatusCode(200);
            response.setDataList(bookingDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings " + e.getMessage());
        }
        return response;
    }



    @Override
@Transactional  // Ensuring atomicity
public Response<BookingDTO> cancelBooking(String bookingId) {
    Response<BookingDTO> response = new Response<>();

    try {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new OurException("Booking Not Found"));

        // Remove the booking from the associated user
        User user = booking.getUser();
        if (user != null && user.getBookings() != null) {
            user.getBookings().removeIf(b -> b.getId().equals(bookingId));
            userRepo.save(user);
        }

        // Remove the booking from the associated accommodation
        Accommodation accommodation = booking.getAccommodation();
        if (accommodation != null && accommodation.getBookings() != null) {
            accommodation.getBookings().removeIf(b -> b.getId().equals(bookingId));
            accommodationRepo.save(accommodation);
        }

        // Delete the booking
        bookingRepo.delete(booking);

        response.setMessage("Booking cancelled successfully.");
        response.setStatusCode(200);

    } catch (OurException e) {
        response.setStatusCode(404);
        response.setMessage(e.getMessage());

    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error cancelling booking: " + e.getMessage());
    }
    return response;
}
    

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                !(bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckInDate()) ||
                  bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckOutDate())));
    }

    @Override
    public Response<BookingDTO> findBookingByConfirmationCode(String confirmationCode) {
        Response<BookingDTO> response = new Response<>();

        try {
            Booking booking = bookingRepo.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking Not Found"));
            // BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOWithUserAndAccommodation(booking, true);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setData(bookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting booking by confirmation code " + e.getMessage());
        }
        return response;
    }    
}

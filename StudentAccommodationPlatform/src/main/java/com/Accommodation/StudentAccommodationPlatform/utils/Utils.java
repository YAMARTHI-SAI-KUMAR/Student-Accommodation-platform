package com.Accommodation.StudentAccommodationPlatform.utils;



import com.Accommodation.StudentAccommodationPlatform.DTO.*;
import com.Accommodation.StudentAccommodationPlatform.Entities.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());

        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
       
        return userDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNoOfCustomers(booking.getNoOfCustomers());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDTO;
    }

    public static AccommodationDTO mapAccommodationEntityToAccommodationDTO(Accommodation accommodation) {
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setId(accommodation.getId());
        accommodationDTO.setBhkType(accommodation.getRoomType());
        accommodationDTO.setAddress(accommodation.getAddress());
        accommodationDTO.setAccommodationType(accommodation.getAccommodationType());
        accommodationDTO.setPrice(accommodation.getPrice());
        // accommodationDTO.setAccommodationUrl(accommodation.getAccommodationUrl());
        accommodationDTO.setAccommodationPhotoUrl(accommodation.getAccommodationPhotoUrl());
        accommodationDTO.setAccommodationDescription(accommodation.getAccommodationDescription());
        accommodationDTO.setAvailable(accommodation.isAvailable());
        return accommodationDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTOWithUserAndAccommodation(Booking booking, boolean includeUser) {
        BookingDTO bookingDTO = mapBookingEntityToBookingDTO(booking);
        
        if (includeUser && booking.getUser() != null) {
            bookingDTO.setUser(mapUserEntityToUserDTO(booking.getUser()));
        }
        
        if (booking.getAccommodation() != null) {
            bookingDTO.setAccommodation(mapAccommodationEntityToAccommodationDTO(booking.getAccommodation()));
        }
        return bookingDTO;
    }

    public static AccommodationDTO mapAccommodationEntityToAccommodationDTOWithBookings(Accommodation accommodation) {
        AccommodationDTO accommodationDTO = mapAccommodationEntityToAccommodationDTO(accommodation);
        if (accommodation.getBookings() != null) {
            accommodationDTO.setBookings(accommodation.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }
        return accommodationDTO;
    }

    public static UserDTO mapUserEntityToUserDTOWithBookings(User user) {
        UserDTO userDTO = mapUserEntityToUserDTO(user);
        if (user.getBookings() != null && !user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOWithUserAndAccommodation(booking, false)).collect(Collectors.toList()));
        }
        return userDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
        UserDTO userDTO = mapUserEntityToUserDTOWithBookings(user);
        
        // If you need to include room details, modify as needed
        return userDTO;
    }
    
    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }

    public static List<AccommodationDTO> mapAccommodationListEntityToAccommodationListDTO(List<Accommodation> accommodationList) {
        return accommodationList.stream().map(Utils::mapAccommodationEntityToAccommodationDTO).collect(Collectors.toList());
    }
}


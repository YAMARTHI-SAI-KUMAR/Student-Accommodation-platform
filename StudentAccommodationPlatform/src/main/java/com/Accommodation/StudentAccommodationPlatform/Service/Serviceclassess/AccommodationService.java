package com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.Accommodation.StudentAccommodationPlatform.DTO.AccommodationDTO;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;

public interface AccommodationService {

 Response<AccommodationDTO> addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

    Response<List<String>> getAllRoomTypes();

    Response<List<AccommodationDTO>> getAllRooms();

    Response<AccommodationDTO> deleteRoom(String roomId);

    Response<AccommodationDTO> updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo);

    Response<AccommodationDTO> getRoomById(String roomId);

    Response<AccommodationDTO> getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    Response<AccommodationDTO> getAllAvailableRooms();
    
}

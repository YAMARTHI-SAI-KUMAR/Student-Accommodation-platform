package com.Accommodation.StudentAccommodationPlatform.Service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Accommodation.StudentAccommodationPlatform.DTO.AccommodationDTO;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.Entities.Accommodation;
import com.Accommodation.StudentAccommodationPlatform.Exception.OurException;
import com.Accommodation.StudentAccommodationPlatform.Repositories.AccommodationRepo;

import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.AccommodationService;
import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.ImageService;
import com.Accommodation.StudentAccommodationPlatform.utils.Utils;

@Service
public class AccommodationServiceImpl implements AccommodationService{


   

   

    @Autowired
    private AccommodationRepo accommodationRepo;

  
  


    @Autowired
    private ImageService imageService;

 

    @Override
public Response<AccommodationDTO> addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
    Response<AccommodationDTO> response = new Response<>();
    
    try {
        // ✅ Validate inputs
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new IllegalArgumentException("Room type cannot be empty");
        }
        if (roomPrice == null || roomPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid room price");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        
        String imageUrl = null;
        
        // ✅ Check if a photo is provided before attempting upload
        if (photo != null && !photo.isEmpty()) {
            // imageUrl = awsS3Service.saveImageToS3(photo);
            imageUrl=imageService.uploadImage(photo);
        }

        // ✅ Create a new Accommodation object
        Accommodation room = new Accommodation();
        room.setAccommodationPhotoUrl(imageUrl);
        room.setRoomType(roomType);
        room.setPrice(roomPrice);
        room.setAccommodationDescription(description);
        
        // ✅ Set the default availability based on type
        if ("permanent".equalsIgnoreCase(roomType)) {
            room.setOccupied(false);
        }

        Accommodation savedRoom = accommodationRepo.save(room);

        AccommodationDTO roomDTO = Utils.mapAccommodationEntityToAccommodationDTO(savedRoom);
        
        response.setStatusCode(200);
        response.setMessage("Room added successfully");
        response.setData(roomDTO);

    } catch (IllegalArgumentException e) {
        response.setStatusCode(400);
        response.setMessage(e.getMessage());
    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error saving a room: " + e.getMessage());
    }

    return response;
}



@Override
public Response<List<String>> getAllRoomTypes() {
    Response<List<String>> response = new Response<>();

    try {
        List<String> roomTypes = accommodationRepo.findDistinctRoomTypes();
        
        if (roomTypes.isEmpty()) {
            response.setStatusCode(204); // No Content
            response.setMessage("No room types available");
        } else {
            response.setStatusCode(200);
            response.setMessage("Room types retrieved successfully");
            response.setData(roomTypes);
        }

    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error fetching room types: " + e.getMessage());
    }

    return response;
}



@Override
public Response<List<AccommodationDTO>> getAllRooms() { 
    Response<List<AccommodationDTO>> response = new Response<>();
    
    try {
        List<Accommodation> roomList = accommodationRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")); // Use createdAt for sorting
        List<AccommodationDTO> roomDTOList = Utils.mapAccommodationListEntityToAccommodationListDTO(roomList);
        
        response.setStatusCode(200);
        response.setMessage("Rooms retrieved successfully");
        response.setData(roomDTOList);

    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error retrieving rooms: " + e.getMessage());
    }
    
    return response; 
}

    @Override
    public Response<AccommodationDTO> deleteRoom(String roomId) { // Change Long to String
        Response<AccommodationDTO> response = new Response<>();
        
        try {
            if (!accommodationRepo.existsById(roomId)) {
                throw new OurException("Room Not Found");
            }
            
            accommodationRepo.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Room deleted successfully");
            
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public Response<AccommodationDTO> updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice,
            MultipartFile photo) {
        Response<AccommodationDTO> response = new Response<>();
    
        try {
            Accommodation room = accommodationRepo.findById(roomId)
                    .orElseThrow(() -> new OurException("Room Not Found"));
    
            // Only update fields if new values are provided
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setPrice(roomPrice);
            if (description != null) room.setAccommodationDescription(description);
    
            // Update image only if a new one is uploaded
            if (photo != null && !photo.isEmpty()) {
                // String imageUrl = awsS3Service.saveImageToS3(photo);
                String imageUrl=imageService.uploadImage(photo);
                room.setAccommodationPhotoUrl(imageUrl);
            }
    
            Accommodation updatedRoom = accommodationRepo.save(room);
            AccommodationDTO roomDTO = Utils.mapAccommodationEntityToAccommodationDTO(updatedRoom);
    
            response.setStatusCode(200);
            response.setMessage("Room updated successfully");
            response.setData(roomDTO);
    
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating room: " + e.getMessage());
        }
        
        return response;
    }
    

    @Override
    public Response<AccommodationDTO> getRoomById(String roomId) {
        Response<AccommodationDTO> response = new Response<>();
    
        try {
            Accommodation room = accommodationRepo.findById(roomId)
                    .orElseThrow(() -> new OurException("Room Not Found"));
    
            AccommodationDTO roomDTO = Utils.mapAccommodationEntityToAccommodationDTOWithBookings(room);
    
            response.setStatusCode(200);
            response.setMessage("Room retrieved successfully");
            response.setData(roomDTO);
    
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving room: " + e.getMessage());
        }
    
        return response;
    }
    

    @Override
public Response<AccommodationDTO> getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate,
        String roomType) {

    Response<AccommodationDTO> response = new Response<>();

    try {
        // Validate date inputs
        if (checkInDate.isAfter(checkOutDate)) {
            response.setStatusCode(400);
            response.setMessage("Invalid date range: Check-in date cannot be after check-out date.");
            return response;
        }

        List<Accommodation> availableRooms = accommodationRepo.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);

        if (availableRooms.isEmpty()) {
            response.setStatusCode(404);
            response.setMessage("No available rooms found for the given date range and type.");
            return response;
        }

        List<AccommodationDTO> roomDTOList = Utils.mapAccommodationListEntityToAccommodationListDTO(availableRooms);

        response.setStatusCode(200);
        response.setMessage("Available rooms retrieved successfully");
        response.setDataList(roomDTOList);

    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error retrieving available rooms: " + e.getMessage());
    }

    return response;
}

@Override
public Response<AccommodationDTO> getAllAvailableRooms() {
    Response<AccommodationDTO> response = new Response<>();
    
    try {
        List<Accommodation> roomList = accommodationRepo.getAllAvailableRooms();

        if (roomList.isEmpty()) {
            response.setStatusCode(404);
            response.setMessage("No available rooms found.");
            return response;
        }

        List<AccommodationDTO> roomDTOList = Utils.mapAccommodationListEntityToAccommodationListDTO(roomList);

        response.setStatusCode(200);
        response.setMessage("Available rooms retrieved successfully.");
        response.setDataList(roomDTOList);

    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error retrieving available rooms: " + e.getMessage());
    }

    return response;
}


        
    
}

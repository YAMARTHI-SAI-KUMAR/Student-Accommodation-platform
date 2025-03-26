package com.Accommodation.StudentAccommodationPlatform.Controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Accommodation.StudentAccommodationPlatform.DTO.AccommodationDTO;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.AccommodationService;
// import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.BookingService;

@RestController
@RequestMapping("/rooms")
public class AccommodationController {

    @Autowired
    private AccommodationService accommodationService;

    // @Autowired
    // private BookingService bookingService;


   @PostMapping("/add")
// @PreAuthorize("hasAuthority('ADMIN')")
public ResponseEntity<Response<AccommodationDTO>> addNewRoom(
        @RequestParam(value = "photo", required = false) MultipartFile photo,
        @RequestParam(value = "roomType", required = false) String roomType,
        @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
        @RequestParam(value = "roomDescription", required = false) String roomDescription
) {
    // Validate Input Fields
    if (photo == null || photo.isEmpty() || 
        roomType == null || roomType.isBlank() || 
        roomPrice == null || 
        roomDescription == null || roomDescription.isBlank()) {

        Response<AccommodationDTO> response = new Response<>();
        response.setStatusCode(400);
        response.setMessage("All fields are required: photo, roomType, roomPrice, roomDescription.");
        return ResponseEntity.badRequest().body(response);
    }

    // Delegate to Service Layer
    Response<AccommodationDTO> response = accommodationService.addNewRoom(photo, roomType, roomPrice, roomDescription);
    return ResponseEntity.status(response.getStatusCode()).body(response);
}

        @GetMapping("/all")
        public ResponseEntity<Response<List<AccommodationDTO>>> getAllRooms() {
            Response<List<AccommodationDTO>> response = accommodationService.getAllRooms();
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }


        @GetMapping("/types")
        public ResponseEntity<Response<List<String>>> getRoomTypes() {
            Response<List<String>> response = accommodationService.getAllRoomTypes();
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        @GetMapping("/room-by-id/{roomId}")
        public ResponseEntity<Response<AccommodationDTO>> getRoomById(@PathVariable String roomId) {
            Response<AccommodationDTO> response = accommodationService.getRoomById(roomId);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        

        @GetMapping("/all-available-rooms")
    public ResponseEntity<Response<AccommodationDTO>> getAvailableRooms() {
        Response<AccommodationDTO> response = accommodationService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response<AccommodationDTO>> getAvailableRoomsByDateAndType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType
    ) {
        if (checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null) {
            Response<AccommodationDTO> response = new Response<>();
            response.setStatusCode(400);
            response.setMessage("Please provide values for all fields(checkInDate, roomType,checkOutDate)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response<AccommodationDTO> response = accommodationService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<AccommodationDTO>> updateRoom(@PathVariable String roomId,
                                               @RequestParam(value = "photo", required = false) MultipartFile photo,
                                               @RequestParam(value = "roomType", required = false) String roomType,
                                               @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
                                               @RequestParam(value = "roomDescription", required = false) String roomDescription

    ) {
        Response<AccommodationDTO> response = accommodationService.updateRoom(roomId, roomDescription, roomType, roomPrice, photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<AccommodationDTO>> deleteRoom(@PathVariable String roomId) {
        Response<AccommodationDTO> response = accommodationService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }
        

    
}

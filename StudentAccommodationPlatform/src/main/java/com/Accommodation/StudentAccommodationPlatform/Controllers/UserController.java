package com.Accommodation.StudentAccommodationPlatform.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.DTO.UserDTO;
import com.Accommodation.StudentAccommodationPlatform.Entities.User;
import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.UserService;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

   
    

    @GetMapping("/all")
     @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<List<UserDTO>>> getAllUsers() {
        Response<List<UserDTO>> response = userService.getAllUsers(); // Ensure generics are correctly returned
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
     @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<Response<UserDTO>> getUserById(@PathVariable("userId") String userId) {
        Response<UserDTO> response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

     @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<UserDTO>> deleteUser(@PathVariable("userId") String userId) {
        Response<UserDTO> response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  
     @GetMapping("/get-logged-in-profile-info")
    public ResponseEntity<Response<UserDTO>> getLoggedInUSerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response<UserDTO> response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    

    
}

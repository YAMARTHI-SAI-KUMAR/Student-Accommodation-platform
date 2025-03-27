package com.Accommodation.StudentAccommodationPlatform.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Accommodation.StudentAccommodationPlatform.DTO.LoginRequest;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.DTO.UserDTO;
import com.Accommodation.StudentAccommodationPlatform.Entities.User;
import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
   public ResponseEntity<Response<UserDTO>> register(@RequestBody User user) {
        Response<UserDTO> response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserDTO>> login(@RequestBody LoginRequest loginRequest) {
        Response<UserDTO> response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    
    
}

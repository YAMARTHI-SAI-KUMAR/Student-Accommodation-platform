package com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess;

import java.util.List;

import com.Accommodation.StudentAccommodationPlatform.DTO.LoginRequest;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.DTO.UserDTO;
import com.Accommodation.StudentAccommodationPlatform.Entities.User;

public interface UserService { 

    Response<UserDTO> register(User user);
    
    Response<UserDTO> login(LoginRequest loginRequest);
    
    Response<List<UserDTO>> getAllUsers();
    
    Response<UserDTO> getUserBookingHistory(String userId);
    
    Response<UserDTO> deleteUser(String userId);
    
    Response<UserDTO> getUserById(String userId);
    
    Response<UserDTO> getMyInfo(String email);
    
    // Additional features
    Response sendVerificationEmail(String email);
    
    Response resetPassword(String email, String newPassword);
    
    Response changePassword(String userId, String oldPassword, String newPassword);
    
    Response updateUser(User user);
    
    Response getSavedAccommodations(String userId);
    
    Response getPaymentHistory(String userId);
    
    Response processPayment(String userId, Double amount);
    
    Response approveListing(String listingId);
    
    Response banUser(String userId);
}



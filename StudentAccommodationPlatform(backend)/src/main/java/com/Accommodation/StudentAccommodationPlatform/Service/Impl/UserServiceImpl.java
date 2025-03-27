package com.Accommodation.StudentAccommodationPlatform.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Accommodation.StudentAccommodationPlatform.DTO.LoginRequest;
import com.Accommodation.StudentAccommodationPlatform.DTO.Response;
import com.Accommodation.StudentAccommodationPlatform.DTO.UserDTO;
import com.Accommodation.StudentAccommodationPlatform.Entities.User;
import com.Accommodation.StudentAccommodationPlatform.Exception.OurException;
import com.Accommodation.StudentAccommodationPlatform.Repositories.UserRepo;
import com.Accommodation.StudentAccommodationPlatform.Security.JwtAuthenticateResource;
import com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess.UserService;
import com.Accommodation.StudentAccommodationPlatform.utils.Utils;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class UserServiceImpl  implements UserService{
  
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtAuthenticateResource jwtAuthenticateResource;

    UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Response<UserDTO> register(User user) {
        Response<UserDTO> response=new Response<UserDTO>();
        try{
             // Assign default role if not provided
            if(user.getRole() == null || user.getRole().isBlank()){ 
                user.setRole("USER");
            }

            //check if email already exists
            if(userRepo.existsById(user.getEmail())){ 
                throw new OurException("Email Already Exists");
            }


              // Encrypt password before saving
              user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

              //save user entity
              User savedUser=userRepo.save(user);

              //Map to Dto
              UserDTO userDTO=Utils.mapUserEntityToUserDTO(savedUser);

              response.setStatusCode(200);
              response.setMessage("Registration Successful");
              response.setData(userDTO);


        }

        catch(OurException e){ 
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        
        catch(Exception e){ 
            response.setStatusCode(500);
            response.setMessage("Error While registering user: "+ e.getMessage());
        }
    
         return response;
        

        
    }

    @Override
    public Response<UserDTO> login(LoginRequest loginRequest) {
        Response<UserDTO>response=new Response<>();
        try{
            //Fetch from DB
            User user=userRepo.findByEmail(loginRequest.getEmail())
                       .orElseThrow(()-> new OurException("Invalid Email or Password"));

             //Check if password match
             if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
                throw new OurException("Invalid Email or Password");

             }

             //Generate JWT token
             Authentication authentication=new UsernamePasswordAuthenticationToken(
                user.getEmail(), null,List.of(new SimpleGrantedAuthority(user.getRole())));
             
                String token=jwtAuthenticateResource.createToken(authentication);

                //convert to DTO
                UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
                    response.setToken(token);

                    response.setStatusCode(200);
                    response.setMessage("Login Successful");
                    response.setData(userDTO);

        }
        catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error While Logging In: " + e.getMessage());
        }




        return response;
        
    }

    @Override
public Response<List<UserDTO>> getAllUsers() {
    Response<List<UserDTO>> response = new Response<>(); // Explicit generic type

    try {
        List<User> userList = userRepo.findAll();
        List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);

        response.setStatusCode(200);
        response.setMessage("Successful");
        response.setData(userDTOList); // Set the List<UserDTO>

    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error while getting all users: " + e.getMessage());
    }

    return response;
}

    

    @Override
    public Response<UserDTO> getUserBookingHistory(String userId) {
        Response<UserDTO>response=new Response<>();
        try {
            User user = userRepo.findById(userId).orElseThrow(()-> new OurException("User Not Found"));
            UserDTO userDTO= Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setData(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage( e.getMessage());

        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while getting user booking history: " +e.getMessage());

        }




        return response;
    }

    


    @Override
public Response<UserDTO> deleteUser(String userId) {
    Response<UserDTO> response = new Response<>();

    try {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new OurException("User Not Found"));

        userRepo.delete(user);

        // Convert the deleted User to UserDTO before returning
        UserDTO userDTO = Utils.mapUserEntityToUserDTO(user); // Assuming you have a constructor or mapper

        response.setStatusCode(200);
        response.setMessage("User deleted successfully");
        response.setData(userDTO);

    } catch (OurException e) {
        response.setStatusCode(404);
        response.setMessage(e.getMessage());
    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error while deleting a user: " + e.getMessage());
    }
    return response;
}



@Override
public Response<UserDTO> getUserById(String userId) {
    Response<UserDTO> response=new Response<>();
    try {
        // Find the user by ID, or throw an exception if not found
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new OurException("User Not Found"));

        // Convert User entity to DTO
        UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

        // Return a successful response with the user DTO
        response.setStatusCode(200);
        response.setData(userDTO);
        response.setMessage("User retrieved successfully");
        return response;

    } catch (OurException e) {
        response.setStatusCode(404);
        response.setMessage(e.getMessage());
        return response;
    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error while retrieving user: " + e.getMessage());
        return response;
    }
}


@Override
public Response<UserDTO> getMyInfo(String email) {
    Response<UserDTO> response = new Response<>();

    try {
        // Find user by email or throw an exception if not found
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new OurException("User Not Found"));

        // Convert User entity to DTO
        UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

        // Set success response
        response.setStatusCode(200);
        response.setMessage("User retrieved successfully");
        response.setData(userDTO);

    } catch (OurException e) {
        response.setStatusCode(404);
        response.setMessage(e.getMessage());
    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("Error while getting user info: " + e.getMessage());
    }

    return response;
}


    @Override
    public Response sendVerificationEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendVerificationEmail'");
    }

    @Override
    public Response resetPassword(String email, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
    }

    @Override
    public Response changePassword(String userId, String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public Response updateUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public Response getSavedAccommodations(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSavedAccommodations'");
    }

    @Override
    public Response getPaymentHistory(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentHistory'");
    }

    @Override
    public Response processPayment(String userId, Double amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }

    @Override
    public Response approveListing(String listingId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveListing'");
    }

    @Override
    public Response banUser(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'banUser'");
    }
    
}

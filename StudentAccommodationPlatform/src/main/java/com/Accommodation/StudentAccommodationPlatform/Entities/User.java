package com.Accommodation.StudentAccommodationPlatform.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
// @Entity
@Document(collection = "user")
public class User implements UserDetails{
    
    @Id
    // @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private String id;// MongoDB automatically generates an ObjectId no need of generatedvalue

    @NotBlank(message="Email is required")
    private String email;

    private String name;

    private String phoneNumber;

  private String password;

    private String role;



    @DBRef
    List<Booking>bookings=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
       return  email;
    }

   

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    




    
}

package com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Accommodation.StudentAccommodationPlatform.Exception.OurException;
import com.Accommodation.StudentAccommodationPlatform.Repositories.UserRepo;


@Service
public class CutsomUserDetailsService  implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepo.findByEmail(username).orElseThrow(() -> new OurException("Username/Email not Found"));
    }
    
}

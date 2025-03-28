package com.Accommodation.StudentAccommodationPlatform.Security;



import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;


 @RestController
public class JwtAuthenticateResource {

    @Autowired
    private JwtEncoder jwtEncoder;

    @PostMapping("/authenticate")
    public JwtResponse authenticate(Authentication authentication) {
        
        return new JwtResponse(createToken(authentication));
            }
        
           
        
        
        
        
    public  String createToken(Authentication authentication) {

               var claims= JwtClaimsSet.builder()
                             .issuer("self")
                             .issuedAt(Instant.now())
                             .expiresAt(Instant.now().plusSeconds(60 * 30))
                             .subject(authentication.getName())
                             .claim("scope", createScope(authentication))
                            .build();
                                             
                 JwtEncoderParameters parameters =JwtEncoderParameters.from((JwtClaimsSet) claims) ;                          
            return jwtEncoder.encode(parameters).getTokenValue();
    }
    private String createScope(Authentication authentication) {
        
        return authentication.getAuthorities().stream()
                      .map(a->a.getAuthority())
                      .collect(Collectors.joining(" "));

      
    }
                                     
                                         
                             
      record JwtResponse(String token){

    }

    
    
    
}


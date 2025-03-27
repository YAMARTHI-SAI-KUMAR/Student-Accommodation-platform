// 

package com.Accommodation.StudentAccommodationPlatform.Security;





import javax.sql.DataSource;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
// import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;


import static org.springframework.security.config.Customizer.withDefaults;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

// import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;




@Configuration
public class JwtSecurityConfiguration {
    @Bean

    
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Frontend domain
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}










   
    @SuppressWarnings("removal")
    @Bean
     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(
            auth ->{
                auth
                .requestMatchers("/auth/**", "/rooms/**", "/bookings/**").permitAll()
                
                   .anyRequest().authenticated();
        }


        );
      http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.sessionManagement(
            session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );


        // http.formLogin();
        // http.httpBasic();
        http.httpBasic(withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions().sameOrigin());


        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        // http.oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));




        return http.build();
    }


 
    // @Bean
    // public DataSource dataSource(){
    //     return new EmbeddedDatabaseBuilder()
    //                .setType(EmbeddedDatabaseType.H2)
    //                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
    //                .build();




    // }
















    // @Bean
    // public UserDetailsService userDetailsService(DataSource dataSource) {
    //     var user = User.withUsername("in28minutes")
    //                 //    .password("{noop}dummy") it is used for there is no encryption
    //                     .password("dummy")
    //                     .passwordEncoder(str -> bCryptPasswordEncoder().encode(str))
    //                    .roles("USER")
    //                    .build();


 
    //     var admin = User.withUsername("admin")
    //                     .password("dummy")
    //                     .passwordEncoder(str -> bCryptPasswordEncoder().encode(str))
    //                     .roles("ADMIN")
    //                     // .roles("ADMIN","USER")


    //                     .build();


    //             var jdbcUserDetailsManager=new  JdbcUserDetailsManager(dataSource);
    //             jdbcUserDetailsManager.createUser(user);
    //             jdbcUserDetailsManager.createUser(admin);


    //             return jdbcUserDetailsManager;
    //     // return new InMemoryUserDetailsManager(user,admin);
    // }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public KeyPair keyPair(){
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException();
        }
       
    }


    @Bean
    public RSAKey rsaKey(KeyPair keypair){


         return new RSAKey
                          .Builder((RSAPublicKey) keypair.getPublic())
                          .privateKey(keypair.getPrivate())
                          .keyID(UUID.randomUUID().toString())
                          .build();
                       


    }


    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey){
        var jwkSet=new JWKSet(rsaKey);
         
        return (jwkSelector,context) -> jwkSelector.select(jwkSet);


       


        // return new JWKSource<SecurityContext>() {
        //     @Override
        //     public  List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException{


        //            return jwkSelector.select(jwkSet);
        //     }
        // };
    }


    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException{
        return NimbusJwtDecoder
               .withPublicKey( rsaKey.toRSAPublicKey())
               .build();


    }
   




    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource){
        return new NimbusJwtEncoder(jwkSource);
    }






}




package com.Accommodation.StudentAccommodationPlatform.Service.Serviceclassess;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
     public String uploadImage(MultipartFile file) throws IOException ;
     public byte[] downloadImage(String fileName);
    
}

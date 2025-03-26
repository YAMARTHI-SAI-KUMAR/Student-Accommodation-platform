package com.Accommodation.StudentAccommodationPlatform.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Column;

import jakarta.persistence.Lob;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection="ImageData")
public class Image {


    @Id
    private String id;

    private String name;
    private String type;
    @Lob
    @Column(name = "imagedata",length = 1000)
    private byte[] imageData;
}

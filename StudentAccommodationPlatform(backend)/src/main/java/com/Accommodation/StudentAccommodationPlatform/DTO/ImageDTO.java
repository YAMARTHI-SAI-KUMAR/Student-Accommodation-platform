package com.Accommodation.StudentAccommodationPlatform.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDTO {
    private String name;
    private String type;
    private byte[] imageData;
}

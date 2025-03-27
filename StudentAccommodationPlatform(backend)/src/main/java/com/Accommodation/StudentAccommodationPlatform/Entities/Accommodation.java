// // package com.Accommodation.StudentAccommodationPlatform.Entities;

// // import java.util.ArrayList;

// // import org.springframework.data.mongodb.core.mapping.DBRef;


// // import org.springframework.data.annotation.Id;
// // import lombok.Data;

// // import java.math.BigDecimal;
// // import java.util.*;

// // @Data
// // public class Accommodation{

// //     @Id
// //     private String id;

// //     private String bhkType;

// //     private String address;

// //     private String accommodationType;//temporary or permanent

// //     private BigDecimal price;

// //     private String accommodationPhotoUrl;
// //     private String accommodationDescription;

// //     private boolean isAvailable;

// //     // @DBRef
// //     // private User user;

// //     @DBRef
// //     List<Booking>bookings=new ArrayList<>()
// // ;
// //     // private Accommodation accommodation;




// // }


// package com.Accommodation.StudentAccommodationPlatform.Entities;

// import lombok.Data;
// import org.springframework.data.annotation.Id;
// import org.springframework.data.mongodb.core.mapping.DBRef;
// import org.springframework.data.mongodb.core.mapping.Document;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;

// @Data
// @Document(collection = "accommodations")
// public class Accommodation {

//     @Id
//     private String id;

//     private String roomType;
//     private String address;
//     private String accommodationType; // temporary or permanent
//     private BigDecimal price;
//     private String accommodationPhotoUrl;
//     private String accommodationDescription;

//     private boolean occupied; // Indicates if a permanent booking exists

//     @DBRef
//     private List<Booking> bookings = new ArrayList<>();

//     public boolean isAvailable() {
//         if ("permanent".equalsIgnoreCase(accommodationType)) {
//             return !occupied;
//         } else if ("temporary".equalsIgnoreCase(accommodationType)) {
//             return bookings.stream().noneMatch(Booking::isActive);
//         }
//         return false;
//     }
// }

package com.Accommodation.StudentAccommodationPlatform.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "accommodations")
public class Accommodation {

    @Id
    private String id;

    private String roomType;
    private String address;
    @NotNull
    private String accommodationType; // temporary or permanent
    private BigDecimal price;
    private String accommodationPhotoUrl;
    private String accommodationDescription;
    
    private boolean occupied; // Indicates if a permanent booking exists

    @DBRef
    private List<Booking> bookings = new ArrayList<>();

    // ✅ Improved logic for room availability
    public boolean isAvailable() {
        if ("permanent".equalsIgnoreCase(accommodationType)) {
            return !occupied;
        } else if ("temporary".equalsIgnoreCase(accommodationType)) {
            return bookings == null || bookings.stream().noneMatch(Booking::isActive);
        }
        return false;
    }

    
}


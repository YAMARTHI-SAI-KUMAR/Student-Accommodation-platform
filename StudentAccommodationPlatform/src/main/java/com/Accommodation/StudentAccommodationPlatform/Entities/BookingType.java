package com.Accommodation.StudentAccommodationPlatform.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BookingType {
    TEMPORARY,
    PERMANENT;

    @JsonCreator
    public static BookingType fromString(String value) {
        for (BookingType type : BookingType.values()) {
            if (type.name().equalsIgnoreCase(value)) {  // Case insensitive comparison
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid booking type: " + value);
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();  // Converts to lowercase for JSON
    }
}


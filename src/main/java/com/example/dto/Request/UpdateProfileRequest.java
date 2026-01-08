package com.example.dto.Request;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UpdateProfileRequest {

    // Валідація тут теж працює
    @Size(min = 10, max = 15)
    private String phoneNumber;

    private String address;
    private String city;

    @Size(max = 1000)
    private String bio;
}
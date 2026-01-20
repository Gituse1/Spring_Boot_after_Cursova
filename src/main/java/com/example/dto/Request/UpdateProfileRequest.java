package com.example.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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
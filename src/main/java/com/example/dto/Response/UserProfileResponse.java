package com.example.dto.Response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String phoneNumber;
    private LocalDate birthDate;
    private String address;
    private String city;
    private String bio;
}

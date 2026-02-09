package com.example.dto.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TripResponse {
    private Long id;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private Integer price;

}
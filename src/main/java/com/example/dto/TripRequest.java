package com.example.dto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TripRequest {
    private Long routeId;
    private Long busId;
    private LocalDateTime departureTime;
}

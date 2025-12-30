package com.example.dto.Request;

import lombok.Data;
import java.time.LocalTime;


@Data
public class RoutePointRequest {
    private Long cityId;
    private double priceFromStart;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
}
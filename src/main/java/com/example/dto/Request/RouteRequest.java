package com.example.dto.Request;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
public class RouteRequest {
    private String routeName;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<RoutePointRequest> points;
}
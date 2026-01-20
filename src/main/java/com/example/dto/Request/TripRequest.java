package com.example.dto.Request;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripRequest {
    private Long routeId;
    private Long busId;
    private LocalDateTime departureTime;
}

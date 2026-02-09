package com.example.dto.Request;

import lombok.Data;

@Data
public class TicketRequest {
    private Long tripId;
    private Long startPointId;
    private Long endPointId;
    private int seatNumber;
}

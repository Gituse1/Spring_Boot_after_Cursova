package com.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketRequest {
    private Long tripId;
    private Long userId;
    private Long startPointId;
    private Long endPointId;
    private int seatNumber;
}

package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketRequest {
    private Long tripId;
    private Long userId;
    private Long startPointId;
    private Long endPointId;
    private String seatNumber;
}

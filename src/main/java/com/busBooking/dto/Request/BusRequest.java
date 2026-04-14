package com.busBooking.dto.Request;

import lombok.Data;

@Data
public class BusRequest {
    private String plate;
    private int capacity;
}

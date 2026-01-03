package com.example.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponse {

    private Long tripId;
    private Long startPointId;
    private Long endPointId;
    private int seatNumber;

}

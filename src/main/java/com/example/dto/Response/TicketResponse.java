package com.example.dto.Response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {

    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int seatNumber;

}

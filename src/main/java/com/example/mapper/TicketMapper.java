package com.example.mapper;

import com.example.dto.Response.TicketResponse;
import com.example.model.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TicketMapper {

    public List<TicketResponse> toResponse(List<Ticket> ticket){
        List<TicketResponse> ticketResponseList = new ArrayList<>();

        for(Ticket i:ticket) {
            TicketResponse ticketResponse = TicketResponse.builder()
                    .startTime(i.getStartPoint().getDepartureTime())
                    .endTime(i.getEndPoint().getArrivalTime())
                    .seatNumber(i.getSeatNumber())
                    .build();
            ticketResponseList.add(ticketResponse);

        }
            return ticketResponseList;
    }
}

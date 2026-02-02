package com.example.mapper;

import com.example.dto.Response.TicketResponse;
import com.example.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TicketMapper {

    public List<TicketResponse> toResponse(List<Ticket> ticket){
        List<TicketResponse> ticketResponseList = new ArrayList<>();

        if(ticket==null) return ticketResponseList;
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

    public Map<Integer,String> ticketToMap(List<Object[]> ticketsSeats,String userEmail){

        Map<Integer,String> ticketMap = new HashMap<>();

        if (ticketsSeats == null) return ticketMap;

        for(Object[] row :ticketsSeats){
            Integer seat=(Integer) row[0];
            String email = ( row[1]).equals(userEmail) ? "MY_SEAT" : "TAKEN";

            ticketMap.put(seat,email);
        }
        return ticketMap;
    }
}

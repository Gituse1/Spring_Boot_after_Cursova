package com.example.dto.Response;

import com.example.repository.TicketRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketStatusResponse {

    private TicketRepository ticketRepository;

    private long id;
    private String status;


}

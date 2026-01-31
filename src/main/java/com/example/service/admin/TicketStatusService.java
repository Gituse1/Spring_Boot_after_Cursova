package com.example.service.admin;

import com.example.dto.Response.TicketStatusResponse;
import com.example.repository.TicketStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Data
public class TicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;

  private List<TicketStatusResponse> getTicketInfo(Long id){

      if(id<=0){
          throw  new IllegalArgumentException("Not valid id");
      }

      return ticketStatusRepository
              .findStatusById(id)
              .orElseThrow(()-> new EntityNotFoundException("TicketStatus Not Found"));
  }

}

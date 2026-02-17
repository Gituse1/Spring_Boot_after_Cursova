package com.example.service.admin;

import com.example.model.TicketStatus;
import com.example.repository.TicketStatusRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Data
public class AdminTicketStatusService {

    private final TicketStatusRepository ticketStatusRepository;

    @Transactional
  private List<TicketStatus> getTicketInfo(Long ticketStatusId){

      if(ticketStatusId<=0){
          throw  new IllegalArgumentException("Not valid id");
      }

      return ticketStatusRepository
              .findStatusByTicketId(ticketStatusId);
  }

}

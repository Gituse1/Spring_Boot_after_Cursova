package com.example.repository;

import com.example.dto.Response.TicketResponse;
import com.example.model.Ticket;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END 
    FROM Ticket t 
    WHERE t.trip.idTrip = :tripId 
    AND t.seatNumber = :seatNumber
""")
    boolean checkSeatIsTaken(@Param("tripId") Long tripId,
                             @Param("seatNumber") String seatNumber);

    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.trip.idTrip = :tripId")
    List<String> findTakenSeatsByTripId(@Param("tripId") Long tripId);

    List<TicketResponse> findByUser(User user);

    @Query("SELECT t FROM Ticket t WHERE t.trip.idTrip = :tripId and t.seatNumber =:seatNumber")
    Ticket findTakenByTripIdAndSeats(@Param("tripId") Long tripId,@Param("seatNumber") int seatNumber);


    @Query("""
        SELECT t.idTicket 
        FROM Ticket t 
        WHERE t.user.email = :email 
          AND t.seatNumber = :seatNumber 
          AND t.startPoint.departureTime = :startTime 
          AND t.endPoint.arrivalTime = :endTime
    """)
    Optional<Long> findIdByDetailsAndTime(
            @Param("email") String email,
            @Param("seatNumber") int seatNumber,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );


}

package com.example.demo.repository;

import com.example.demo.model.Ticket;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Ticket t WHERE t.trip.idTrip = :tripId " +
            "AND t.seatNumber = :seatNumber")
    boolean checkSeatIsTaken(@Param("tripId") Long tripId,
                             @Param("seatNumber") String seatNumber);

    @Query("SELECT t.seatNumber FROM Ticket t WHERE t.trip.idTrip = :tripId")
    List<String> findTakenSeatsByTripId(@Param("tripId") Long tripId);

    List<Ticket> findByUser(User user);

}

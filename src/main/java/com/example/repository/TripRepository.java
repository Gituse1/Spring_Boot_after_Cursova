package com.example.repository;

import com.example.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip,Long> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Trip t " +
            "JOIN t.route r " +
            "JOIN r.routePoints p " +
            "WHERE t.bus.idBus = :busId " +
            "AND p.arrivalTime > :newStartTime " +
            "AND t.departureTime < :newEndTime")
    boolean checkBusIsBusy(@Param("busId") Long busId,
                           @Param("newStartTime") LocalTime newStartTime,
                           @Param("newEndTime") LocalTime newEndTime);

    @Query("SELECT t FROM Trip t " +
            "JOIN t.route r " +
            "JOIN r.routePoints p1 " +
            "JOIN r.routePoints p2 " +
            "WHERE CAST(t.departureTime AS date) = :searchDate " +
            "AND p1.city.idCity = :fromId " +
            "AND p2.city.idCity = :toId " +
            "AND p1.orderIndex < p2.orderIndex")
    List<Trip> findTripsByRouteAndDate(
            @Param("fromId") Long fromId,
            @Param("toId") Long toId,
            @Param("searchDate") LocalDate searchDate
    );

    @Query("""
           SELECT COUNT(t) > 0 
           FROM Ticket t 
           WHERE t.trip.id = :tripId 
             AND t.seatNumber = :seatNumber
             AND (
                  (t.startPoint.orderIndex < :endOrderIndex AND t.endPoint.orderIndex > :startOrderIndex)
             )
           """)
    boolean isSeatOccupied(
            @Param("tripId") Long tripId,
            @Param("seatNumber") int seatNumber,
            @Param("startOrderIndex") long startOrderIndex,
            @Param("endOrderIndex") long endOrderIndex
    );




}

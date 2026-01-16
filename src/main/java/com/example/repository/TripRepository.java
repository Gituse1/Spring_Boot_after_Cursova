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

@Repository
public interface TripRepository extends JpaRepository<Trip,Long> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Trip t " +
            "WHERE t.bus.idBus = :busId " +
            // Логіка: (StartA < EndB) AND (EndA > StartB)
            "AND (t.departureTime < :newEndTime AND t.arrivalTime > :newStartTime)")
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
}

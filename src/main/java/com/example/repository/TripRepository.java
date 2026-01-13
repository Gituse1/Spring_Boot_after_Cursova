package com.example.repository;

import com.example.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TripRepository extends JpaRepository<Trip,Long> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Trip t " +
            "WHERE t.bus.idBus = :busId " +
            // Логіка: (StartA < EndB) AND (EndA > StartB)
            "AND (t.departureTime < :newEndTime AND t.arrivalTime > :newStartTime)")
    boolean checkBusIsBusy(@Param("busId") Long busId,
                           @Param("newStartTime") LocalDateTime newStartTime,
                           @Param("newEndTime") LocalDateTime newEndTime);
}

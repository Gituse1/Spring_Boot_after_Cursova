package com.busBooking.repository;

import com.busBooking.model.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePointRepository extends JpaRepository<RoutePoint,Long> {
}

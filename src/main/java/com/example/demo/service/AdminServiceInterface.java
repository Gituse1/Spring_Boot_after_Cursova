package com.example.demo.service;

import com.example.demo.dto.RouteRequest;
import com.example.demo.model.Bus;
import com.example.demo.model.City;
import com.example.demo.model.Route;
import com.example.demo.model.Trip;
import jakarta.transaction.Transactional;

public interface AdminServiceInterface {
    City createCity(String name);

    Bus createBus(String plateNumber, int capacity);

    @Transactional
    Route createRoute(RouteRequest request);

    Trip scheduleTrip(Long routeId, Long busId, String departureTime);
}

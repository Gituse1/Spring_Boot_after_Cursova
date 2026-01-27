package com.example.service.admin;

import com.example.dto.Request.TripRequest;
import com.example.model.*;
import com.example.repository.BusRepository;
import com.example.repository.RouteRepository;
import com.example.repository.TripRepository;
import com.example.service.AuditService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class AdminTripService {

    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final TripRepository tripRepository;
    private final AuditService auditService;


    @Transactional
    public Trip createTrip(TripRequest request) {

        LocalTime startRange = LocalTime.from(request.getDepartureTime().minusHours(1));
        LocalTime endRange = LocalTime.from(request.getDepartureTime().plusHours(1));

        // 1. ВАЛІДАЦІЯ: Передаємо діапазон у репозиторій
        boolean isBusBusy = tripRepository.checkBusIsBusy(
                request.getBusId(),
                startRange,
                endRange
        );

        if (isBusBusy) {
            auditService.log(ActionType.ADMIN_TRIP_CREATE_TRIP_BUS_IS_BUSY, LevelLogin.ERROR);
            throw new IllegalArgumentException("Bus is busy! It has another trip between " + startRange + " and " + endRange);
        }

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> {
                    auditService.log(ActionType.ADMIN_TRIP_CREATE_TRIP_ROUTE_NOT_FOUND, LevelLogin.ERROR);
                    return new EntityNotFoundException("Route not found with id: " + request.getRouteId());
                });


        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> {
                    auditService.log(ActionType.ADMIN_TRIP_CREATE_TRIP_BUS_NOT_FOUND, LevelLogin.ERROR);
                    return new EntityNotFoundException("Bus not found with id: " + request.getBusId());
                });

        auditService.log(ActionType.ADMIN_TRIP_CREATE_TRIP_CREATED, LevelLogin.INFO);

        Trip trip = Trip.builder()
                .route(route)
                .bus(bus)
                .departureTime(request.getDepartureTime())
                .build();

        return tripRepository.save(trip);
    }
}

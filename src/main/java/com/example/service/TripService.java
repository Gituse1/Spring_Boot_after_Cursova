package com.example.service;

import com.example.dto.Request.TripRequest;
import com.example.model.*;
import com.example.repository.BusRepository;
import com.example.repository.RouteRepository;
import com.example.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripService  {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
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
            auditService.createNewLog(ActionType.CREATE_TRIP,false);
            throw new IllegalArgumentException("Bus is busy! It has another trip between " + startRange + " and " + endRange);
        }

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> {
                    auditService.createNewLog(ActionType.CREATE_TRIP, false);
                    return new EntityNotFoundException("Route not found with id: " + request.getRouteId());
                });

        auditService.createNewLog(ActionType.CREATE_TRIP, true);

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> {
                    auditService.createNewLog(ActionType.CREATE_TRIP, false);
                    return new EntityNotFoundException("Bus not found with id: " + request.getBusId());
                });

        auditService.createNewLog(ActionType.FIND_BUS, true);

        Trip trip = new Trip();
        trip.setRoute(route);
        trip.setBus(bus);
        trip.setDepartureTime(request.getDepartureTime());

        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Transactional
    public List<Trip> searchTrips(Long fromCityId, Long toCityId, String dateStr) {
        // Якщо дата прийшла, перетворюємо її. Якщо ні — буде null.
        if (dateStr == null || dateStr.isEmpty()){
            throw new IllegalArgumentException("Не відповідна дата");
        }

        try {
            // Цей метод очікує формат "2023-12-31" за замовчуванням
            LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException("Невірний формат дати: '" + dateStr + "'. Очікується формат РРРР-ММ-ДД (наприклад 2025-01-20).");
        }
        LocalDate searchDate = LocalDate.parse(dateStr);

        return tripRepository.findTripsByRouteAndDate(fromCityId,toCityId,LocalDate.parse(dateStr));

    }
}

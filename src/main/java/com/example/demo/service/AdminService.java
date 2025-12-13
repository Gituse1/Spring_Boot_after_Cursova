package com.example.demo.service;
import com.example.demo.dto.RoutePointRequest;
import com.example.demo.dto.RouteRequest;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AdminService implements AdminServiceInterface {

    private final CityRepository cityRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final RoutePointRepository routePointRepository;
    private final TripRepository tripRepository;


    public City createCity(String name) {
        // 1. Перевірка: чи є вже таке місто?
        if (cityRepository.existsByName(name)) {
            throw new IllegalArgumentException("Місто з назвою '" + name + "' вже існує!");
        }

        // 2. Якщо немає - створюємо нове
        City city = new City();
        city.setName(name);

        return cityRepository.save(city);
    }

    @Override
    public Bus createBus(String plateNumber, int capacity) {
        Bus bus = new Bus();
        bus.setPlateNumber(plateNumber);
        bus.setTotalSeats(capacity);
        return busRepository.save(bus);
    }

    @Transactional
    @Override
    public Route createRoute(RouteRequest request) {
        // 1. Створюємо "шапку" маршруту
        Route route = new Route();
        route.setNameRoute(request.getRouteName());
        route = routeRepository.save(route);

        // 2. Створюємо точки
        int orderIndex = 0;
        for (RoutePointRequest pointDto : request.getPoints()) {
            orderIndex++;

            City city = cityRepository.findById(pointDto.getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found: " + pointDto.getCityId()));

            RoutePoint point = new RoutePoint();
            point.setRoute(route);
            point.setCity(city);
            point.setOrderIndex(orderIndex);
            point.setPrice( pointDto.getPriceFromStart());

            routePointRepository.save(point);
        }
        return route;
    }

    @Override
    public Trip scheduleTrip(Long routeId, Long busId, String departureTimeStr) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        Trip trip = new Trip();
        trip.setRoute(route);
        trip.setBus(bus);
        trip.setDepartureTime(LocalDateTime.parse(departureTimeStr)); // Формат дати має бути "2025-12-10T15:30:00"

        return tripRepository.save(trip);
    }
}
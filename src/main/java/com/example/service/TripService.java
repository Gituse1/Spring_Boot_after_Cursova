package com.example.service;

import com.example.dto.Request.TripRequest;
import com.example.model.Bus;
import com.example.model.Route;
import com.example.model.RoutePoint;
import com.example.model.Trip;
import com.example.repository.BusRepository;
import com.example.repository.RouteRepository;
import com.example.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService  {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;


    public Trip createTrip(TripRequest request) {

        LocalDateTime startRange = request.getDepartureTime().minusHours(1);
        LocalDateTime endRange = request.getDepartureTime().plusHours(1);

        // 1. ВАЛІДАЦІЯ: Передаємо діапазон у репозиторій
        boolean isBusBusy = tripRepository.checkBusIsBusy(
                request.getBusId(),
                startRange,
                endRange
        );

        if (isBusBusy) {
            throw new IllegalArgumentException("Bus is busy! It has another trip between " + startRange + " and " + endRange);
        }
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + request.getRouteId()));
        // Знаходимо автобус
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new EntityNotFoundException("Bus not found with id: " + request.getBusId()));

        // Створюємо рейс
        Trip trip = new Trip();
        trip.setRoute(route);
        trip.setBus(bus);
        trip.setDepartureTime(request.getDepartureTime());

        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public List<Trip> searchTrips(Long fromCityId, Long toCityId, String dateStr) {
        // Якщо дата прийшла, перетворюємо її. Якщо ні — буде null.
        LocalDate searchDate = (dateStr != null && !dateStr.isEmpty()) ? LocalDate.parse(dateStr) : null;

        return tripRepository.findAll().stream()
                .filter(trip -> {
                    // 1. Фільтр по ДАТІ (якщо вона обрана)
                    if (searchDate != null) {
                        if (!trip.getDepartureTime().toLocalDate().equals(searchDate)) {
                            return false; // Дата не співпала
                        }
                    }

                    // 2. Фільтр по МАРШРУТУ
                    // Якщо міста не вибрані — вважаємо, що вони "знайдені"
                    boolean fromFound = (fromCityId == null);
                    boolean toFound = (toCityId == null);

                    int fromIndex = -1;
                    int toIndex = -1;

                    // Пробігаємось по точках маршруту
                    for (RoutePoint point : trip.getRoute().getRoutePoints()) {
                        if (fromCityId != null && point.getCity().getIdCity() == fromCityId) {
                            fromIndex = point.getOrderIndex();
                            fromFound = true;
                        }
                        if (toCityId != null && point.getCity().getIdCity() == toCityId) {
                            toIndex = point.getOrderIndex();
                            toFound = true;
                        }
                    }

                    // Якщо якесь із вибраних міст не знайдено — рейс не підходить
                    if (!fromFound || !toFound) return false;

                    // Якщо вибрані ОБИДВА міста — перевіряємо порядок (щоб не їхати назад)
                    if (fromCityId != null && toCityId != null) {
                        return fromIndex < toIndex;
                    }

                    return true;
                })
                .toList();
    }
}

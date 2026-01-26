package com.example.service.user;

import com.example.model.*;
import com.example.repository.TripRepository;
import com.example.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService  {

    private final TripRepository tripRepository;
    private final AuditService auditService;


    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Transactional
    public List<Trip> searchTrips(Long fromCityId, Long toCityId, String dateStr) {

        if (dateStr == null || dateStr.isEmpty()){
            auditService.log(ActionType.USER_TRIP_SEARCH_TRIP_DATE_NOT_VALID_FORMAT, LevelLogin.ERROR);
            throw new IllegalArgumentException("Не відповідна дата");
        }

        try {
            // Цей метод очікує формат "2023-12-31" за замовчуванням
            LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            auditService.log(ActionType.USER_TRIP_SEARCH_TRIP_DATE_NOT_VALID_FORMAT, LevelLogin.ERROR);
            throw new IllegalArgumentException("Невірний формат дати: '" + dateStr + "'. Очікується формат РРРР-ММ-ДД (наприклад 2025-01-20).");
        }
        LocalDate searchDate = LocalDate.parse(dateStr);


        return tripRepository.findTripsByRouteAndDate(fromCityId, toCityId, searchDate);

    }
}

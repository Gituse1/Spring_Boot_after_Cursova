package com.example.demo.controller;

import com.example.demo.dto.TripRequest;
import com.example.demo.model.Trip;
import com.example.demo.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    // --- БУЛО: testConnection ---
    // --- СТАЛО: Отримання списку рейсів ---
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        // Цей метод ми зараз додамо в сервіс
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequest tripRequest) {
        Trip newTrip = tripService.createTrip(tripRequest);
        return ResponseEntity.ok(newTrip);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam(required = false) Long fromCityId, // <-- Змінено
            @RequestParam(required = false) Long toCityId,   // <-- Змінено
            @RequestParam(required = false) String date) {   // <-- Змінено

        return ResponseEntity.ok(tripService.searchTrips(fromCityId, toCityId, date));
    }
}
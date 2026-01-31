package com.example.controller.user;

import com.example.model.Trip;
import com.example.service.user.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService tripService;

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }


    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam(required = false) Long fromCityId,
            @RequestParam(required = false) Long toCityId,
            @RequestParam(required = false) String date) {

        return ResponseEntity.ok(tripService.searchTrips(fromCityId, toCityId, date));
    }
}
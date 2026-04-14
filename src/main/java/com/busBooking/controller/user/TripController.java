package com.busBooking.controller.user;

import com.busBooking.dto.Response.TripResponse;
import com.busBooking.model.Trip;
import com.busBooking.service.user.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<TripResponse>> getAllTrips(@PageableDefault(page = 0, size = 10, sort = "createdAt") Pageable pageable)
    {

        if (pageable.getPageSize() > 50) {
            pageable = PageRequest.of(pageable.getPageNumber(), 50, pageable.getSort());
        }

        return ResponseEntity.ok(tripService.getAllTrips(pageable));
    }


    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam(required = false) Long fromCityId,
            @RequestParam(required = false) Long toCityId,
            @RequestParam(required = false) String date) {

        return ResponseEntity.ok(tripService.searchTrips(fromCityId, toCityId, date));
    }
}
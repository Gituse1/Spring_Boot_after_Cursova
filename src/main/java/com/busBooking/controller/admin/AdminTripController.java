package com.busBooking.controller.admin;

import com.busBooking.dto.Request.TripRequest;
import com.busBooking.model.Trip;
import com.busBooking.service.admin.AdminTripService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("api/admin/trip")
@RestController
public class AdminTripController {

    private final AdminTripService adminTripService;

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequest tripRequest) {
        Trip newTrip = adminTripService.createTrip(tripRequest);
        return ResponseEntity.ok(newTrip);
    }
}

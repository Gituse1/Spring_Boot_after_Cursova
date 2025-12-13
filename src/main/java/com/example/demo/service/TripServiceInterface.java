package com.example.demo.service;

import com.example.demo.dto.TripRequest;
import com.example.demo.model.Trip;

public interface TripServiceInterface {
    public Trip createTrip(TripRequest request);
}

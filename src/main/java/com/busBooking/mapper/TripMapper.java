package com.busBooking.mapper;

import com.busBooking.dto.Response.TripResponse;
import com.busBooking.model.Route;
import com.busBooking.model.RoutePoint;
import com.busBooking.model.Trip;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component

public class TripMapper {

    public TripResponse toResponse(Trip trip) {
        if (trip == null) return null;
        TripResponse response = new TripResponse();


        response.setId(trip.getIdTrip());
        response.setDepartureTime(trip.getDepartureTime());
        response.setPrice(trip.getPrice());
        List<RoutePoint> points = Optional.ofNullable(trip.getRoute())
                .map(Route::getRoutePoints)
                .orElse(Collections.emptyList());

        if (!points.isEmpty()) {
            response.setDepartureCity(points.get(0).getCity().getName());
            response.setArrivalCity(points.get(points.size() - 1).getCity().getName());
        }


        return response;
    }
}

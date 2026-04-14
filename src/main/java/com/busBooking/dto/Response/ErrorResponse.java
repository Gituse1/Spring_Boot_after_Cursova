package com.busBooking.dto.Response;

public record ErrorResponse(
        int status,
        String error,
        String message,
        long timestamp
) {}
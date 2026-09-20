package com.example.moviebooking.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record BookingRequest(
        @NotNull(message = "showId is required")
        Long showId,

        @NotBlank(message = "customerName is required")
        @Size(max = 100, message = "customerName is too long")
        String customerName,

        @NotBlank(message = "customerEmail is required")
        @Email(message = "customerEmail is not a valid email")
        String customerEmail,

        @NotEmpty(message = "select at least one seat")
        @Size(max = 10, message = "you can book at most 10 seats at once")
        List<String> seats
) {}
package com.example.moviebooking.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(String bookingCode, String status, String movieTitle,
                              String screenName, LocalDateTime showTime, List<String> seats,
                              String customerName, String customerEmail,
                              BigDecimal totalAmount, Instant createdAt) {}
package com.example.moviebooking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SeatMapResponse(Long showId, String movieTitle, String screenName,
                              LocalDateTime showTime, BigDecimal pricePerSeat,
                              int totalRows, int seatsPerRow, List<String> bookedSeats) {}
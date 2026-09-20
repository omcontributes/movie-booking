package com.example.moviebooking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShowResponse(Long id, Long movieId, String movieTitle, String screenName,
                           LocalDateTime showTime, BigDecimal pricePerSeat,
                           int totalRows, int seatsPerRow) {}
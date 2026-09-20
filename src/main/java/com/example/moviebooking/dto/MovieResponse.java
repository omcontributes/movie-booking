package com.example.moviebooking.dto;

public record MovieResponse(Long id, String title, String description,
                            String genre, String language, int durationMinutes) {}
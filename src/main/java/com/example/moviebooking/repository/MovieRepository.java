package com.example.moviebooking.repository;

import com.example.moviebooking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
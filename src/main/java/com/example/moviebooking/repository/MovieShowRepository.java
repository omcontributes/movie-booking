package com.example.moviebooking.repository;

import com.example.moviebooking.model.MovieShow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieShowRepository extends JpaRepository<MovieShow, Long> {

    List<MovieShow> findByMovieIdOrderByShowTimeAsc(Long movieId);
}

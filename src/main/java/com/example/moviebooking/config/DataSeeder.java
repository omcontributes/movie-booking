package com.example.moviebooking.config;

import com.example.moviebooking.model.Movie;
import com.example.moviebooking.model.MovieShow;
import com.example.moviebooking.repository.MovieRepository;
import com.example.moviebooking.repository.MovieShowRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final int ROWS = 8;            // rows A-H
    private static final int SEATS_PER_ROW = 10;  // seats 1-10

    private final MovieRepository movieRepository;
    private final MovieShowRepository showRepository;

    public DataSeeder(MovieRepository movieRepository, MovieShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            return;
        }

        List<Movie> movies = movieRepository.saveAll(List.of(
                new Movie("Galaxy Runners",
                        "A crew of smugglers races across the galaxy to deliver one impossible cargo.",
                        "Sci-Fi", "English", 128),
                new Movie("The Last Monsoon",
                        "A small-town family faces one final season that changes everything.",
                        "Drama", "Hindi", 142),
                new Movie("Laugh Factory",
                        "Three friends open a comedy club and everything that can go wrong does.",
                        "Comedy", "English", 105),
                new Movie("Code Red",
                        "A young hacker uncovers a plot hidden inside a city's power grid.",
                        "Thriller", "English", 118)
        ));

        LocalTime[] slots = {LocalTime.of(10, 0), LocalTime.of(14, 0),
                LocalTime.of(18, 30), LocalTime.of(21, 45)};
        int[] prices = {200, 250, 300, 300};

        LocalDateTime now = LocalDateTime.now();
        List<MovieShow> shows = new ArrayList<>();

        for (int m = 0; m < movies.size(); m++) {
            Movie movie = movies.get(m);
            String screen = "Screen " + (m % 3 + 1);
            for (int day = 0; day < 4; day++) {
                for (int s = 0; s < slots.length; s++) {
                    LocalDateTime time = LocalDate.now().plusDays(day).atTime(slots[s]);
                    if (time.isAfter(now)) {
                        shows.add(new MovieShow(movie, screen, time,
                                BigDecimal.valueOf(prices[s]), ROWS, SEATS_PER_ROW));
                    }
                }
            }
        }
        showRepository.saveAll(shows);
    }
}
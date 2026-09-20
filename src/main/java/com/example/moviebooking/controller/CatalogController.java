package com.example.moviebooking.controller;

import com.example.moviebooking.dto.MovieResponse;
import com.example.moviebooking.dto.SeatMapResponse;
import com.example.moviebooking.dto.ShowResponse;
import com.example.moviebooking.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/movies")
    public List<MovieResponse> movies() {
        return catalogService.listMovies();
    }

    @GetMapping("/movies/{movieId}/shows")
    public List<ShowResponse> shows(@PathVariable Long movieId) {
        return catalogService.showsForMovie(movieId);
    }

    @GetMapping("/shows/{showId}/seats")
    public SeatMapResponse seats(@PathVariable Long showId) {
        return catalogService.seatMap(showId);
    }
}

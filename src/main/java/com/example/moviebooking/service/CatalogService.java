package com.example.moviebooking.service;

import com.example.moviebooking.dto.MovieResponse;
import com.example.moviebooking.dto.SeatMapResponse;
import com.example.moviebooking.dto.ShowResponse;
import com.example.moviebooking.exception.NotFoundException;
import com.example.moviebooking.model.Movie;
import com.example.moviebooking.model.MovieShow;
import com.example.moviebooking.repository.BookedSeatRepository;
import com.example.moviebooking.repository.MovieRepository;
import com.example.moviebooking.repository.MovieShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {

    private final MovieRepository movieRepository;
    private final MovieShowRepository showRepository;
    private final BookedSeatRepository bookedSeatRepository;

    public CatalogService(MovieRepository movieRepository,
                          MovieShowRepository showRepository,
                          BookedSeatRepository bookedSeatRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.bookedSeatRepository = bookedSeatRepository;
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> listMovies() {
        return movieRepository.findAll().stream()
                .map(m -> new MovieResponse(m.getId(), m.getTitle(), m.getDescription(),
                        m.getGenre(), m.getLanguage(), m.getDurationMinutes()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShowResponse> showsForMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        return showRepository.findByMovieIdOrderByShowTimeAsc(movieId).stream()
                .map(s -> new ShowResponse(s.getId(), movie.getId(), movie.getTitle(),
                        s.getScreenName(), s.getShowTime(), s.getPricePerSeat(),
                        s.getTotalRows(), s.getSeatsPerRow()))
                .toList();
    }

    @Transactional(readOnly = true)
    public SeatMapResponse seatMap(Long showId) {
        MovieShow show = showRepository.findById(showId)
                .orElseThrow(() -> new NotFoundException("Show not found"));
        List<String> booked = bookedSeatRepository.findSeatNumbersByShowId(showId);
        return new SeatMapResponse(show.getId(), show.getMovie().getTitle(), show.getScreenName(),
                show.getShowTime(), show.getPricePerSeat(),
                show.getTotalRows(), show.getSeatsPerRow(), booked);
    }
}
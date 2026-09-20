package com.example.moviebooking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_show")
public class MovieShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(nullable = false)
    private String screenName;

    @Column(nullable = false)
    private LocalDateTime showTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerSeat;

    private int totalRows;
    private int seatsPerRow;

    protected MovieShow() {}

    public MovieShow(Movie movie, String screenName, LocalDateTime showTime,
                     BigDecimal pricePerSeat, int totalRows, int seatsPerRow) {
        this.movie = movie;
        this.screenName = screenName;
        this.showTime = showTime;
        this.pricePerSeat = pricePerSeat;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
    }

    public Long getId() { return id; }
    public Movie getMovie() { return movie; }
    public String getScreenName() { return screenName; }
    public LocalDateTime getShowTime() { return showTime; }
    public BigDecimal getPricePerSeat() { return pricePerSeat; }
    public int getTotalRows() { return totalRows; }
    public int getSeatsPerRow() { return seatsPerRow; }
}
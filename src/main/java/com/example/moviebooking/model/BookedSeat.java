package com.example.moviebooking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "booked_seat",
        uniqueConstraints = @UniqueConstraint(name = "uk_show_seat", columnNames = {"show_id", "seat_number"}))
public class BookedSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(optional = false)
    @JoinColumn(name = "show_id")
    private MovieShow movieShow;

    @Column(name = "seat_number", nullable = false, length = 8)
    private String seatNumber;

    protected BookedSeat() {}

    public BookedSeat(MovieShow movieShow, String seatNumber) {
        this.movieShow = movieShow;
        this.seatNumber = seatNumber;
    }

    void setBooking(Booking booking) { this.booking = booking; }

    public Long getId() { return id; }
    public Booking getBooking() { return booking; }
    public MovieShow getMovieShow() { return movieShow; }
    public String getSeatNumber() { return seatNumber; }
}
package com.example.moviebooking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String bookingCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "show_id")
    private MovieShow movieShow;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedSeat> seats = new ArrayList<>();

    protected Booking() {}

    public Booking(String bookingCode, MovieShow movieShow, String customerName,
                   String customerEmail, BigDecimal totalAmount) {
        this.bookingCode = bookingCode;
        this.movieShow = movieShow;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
    }

    public void addSeat(BookedSeat seat) {
        seats.add(seat);
        seat.setBooking(this);
    }

    /** Marks the booking cancelled and releases its seats. */
    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        this.seats.clear();
    }

    public Long getId() { return id; }
    public String getBookingCode() { return bookingCode; }
    public MovieShow getMovieShow() { return movieShow; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public BookingStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Instant getCreatedAt() { return createdAt; }
    public List<BookedSeat> getSeats() { return seats; }
}